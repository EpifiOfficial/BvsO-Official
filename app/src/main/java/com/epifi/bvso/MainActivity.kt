package com.epifi.bvso

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.epifi.bvso.databinding.ActivityMainBinding
import com.epifi.bvso.models.TransactionModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import drewcarlson.coingecko.CoinGeckoClient
import drewcarlson.coingecko.models.coins.CoinPrice
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() ,(TransactionModel) -> Unit {
    lateinit var FabAddTracsaction:FloatingActionButton
    private var openTransactionsList:List<TransactionModel> = ArrayList()
    private var openTransactionAdapter: OpenTransactionAdapter = OpenTransactionAdapter(openTransactionsList,this)
    lateinit var fireStore : FirebaseFirestore
    lateinit var recyclerView: RecyclerView
    lateinit var binding :ActivityMainBinding
    lateinit var transition:MotionLayout
    private lateinit var auth: FirebaseAuth
    lateinit var Tv_gain_lose :TextView
    lateinit var relativeLayout: RelativeLayout




    public override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        //If is clicked send the user to another activity to be able upload the transaction
        FabAddTracsaction.setOnClickListener {

                val transition = Intent(this, TransactionsActivity::class.java)
                startActivity(transition)


        }
        downLoadTransactions()

        relativeLayout.setOnClickListener {
            transition.transitionToStart()
        }


    }

    fun init(){
        transition = findViewById(R.id.Ml)
        relativeLayout = findViewById(R.id.relativeLayout3)
        Tv_gain_lose = findViewById(R.id.Tv_Gain_Lose)
        FabAddTracsaction = findViewById(R.id.Fab_AddTransaction)
        recyclerView = findViewById(R.id.RvHomeLayout)
        val layoutManager = AutoFitGridLayoutManage(this, 300)
        recyclerView.layoutManager = layoutManager
        //recyclerView.layoutManager = GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false)
        recyclerView.adapter = openTransactionAdapter
        // Initialize Firebase Auth and firestore
        auth = Firebase.auth
        fireStore = Firebase.firestore
    }
    fun updateUI(firebaseUser: FirebaseUser?){

        when{
            firebaseUser==null ->{
                auth.signInAnonymously()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success")
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }
            }
            else->
                Toast.makeText(this,"Bienvenido",Toast.LENGTH_LONG).show()

        }

    }
    private fun downLoadTransactions(){
        val uID = auth.uid

        fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("openTransactions").orderBy("uid").startAt(uID.toString())
            .endAt("${uID.toString()}\uf8ff")
            .get()
            .addOnCompleteListener { it ->

                openTransactionsList = it.result!!.toObjects(TransactionModel::class.java)
                openTransactionAdapter.transactionsList = openTransactionsList
                openTransactionAdapter.notifyDataSetChanged()

            }
            .addOnFailureListener { exception ->

                Log.w(TAG, "Error getting documents: ", exception)
                Toast.makeText(this,"Error getting documents",Toast.LENGTH_SHORT).show()

            }
    }
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()

            .baseUrl("https://api.coingecko.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
    override fun invoke(p1: TransactionModel) {
        transition.transitionToStart()

        val amountCoinBought = p1.amountCoinBought
        val priceCoinBought = p1.priceCoinBought
        val typeCryptoCoin = p1.typeCoin
        val marketPriceCoinBought = priceCoinBought!! / amountCoinBought!!
        Toast.makeText(this,typeCryptoCoin.toString(),Toast.LENGTH_SHORT).show()

           // transition.transitionToStart()

        CoroutineScope(Dispatchers.IO).launch {

            val coinGecko = CoinGeckoClient.create()
             val coinCurrentPrice = coinGecko.getPrice(typeCryptoCoin.toString().lowercase(),"usd",false,false,false,false)
            runOnUiThread {
                if (coinCurrentPrice.isNullOrEmpty()) {
                    Log.d("ayush: ", "Something went wrong")
                    Tv_gain_lose.text = "No data of "+typeCryptoCoin.toString()
                    transition.transitionToEnd()

                } else {
                    Log.d("ayush:",coinCurrentPrice.toString())
                    var replacement = coinCurrentPrice.toString().replace("[^0-9-.]".toRegex(), "")
                    val checkAmount = amountCoinBought*replacement.toDouble()
                    //Rounding the number
                    val df = DecimalFormat("#.###")
                    df.roundingMode = RoundingMode.CEILING
                    val gain_lose = df.format(checkAmount-priceCoinBought.toDouble())
                    Tv_gain_lose.text = gain_lose.toString()
                    transition.transitionToEnd()

                }
            }
        }
    }
}