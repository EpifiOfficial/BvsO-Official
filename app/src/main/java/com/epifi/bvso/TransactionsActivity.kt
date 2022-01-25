package com.epifi.bvso

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.epifi.bvso.models.TransactionModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TransactionsActivity : AppCompatActivity() {
    lateinit var Et_AmountCoinBought: EditText
    lateinit var Et_PriceCoinBought:EditText
    lateinit var Et_AmountCoinSold: EditText
    lateinit var Et_PriceCoinSold:EditText
    lateinit var Et_dateCoinSold:EditText
    lateinit var Et_dateCoinBought:EditText
    lateinit var BtnDoneSoldTransaction:TextView
    lateinit var BtnDoneBoughtTransaction:TextView
    lateinit var BtnNext:TextView

    lateinit var typeCoin: String
    lateinit var amountCoinBought:String
    lateinit var priceCoinBought:String
    lateinit var amountCoinSold:String
    lateinit var priceCoinSold:String
    lateinit var dateCoinSold:String
    lateinit var dateCoinBought:String
    lateinit var cellChooseCoin:View
    lateinit var cellAddBoughtTransaction:View
    lateinit var cellAddSoldTransaction:View

    lateinit var btnBitcoin:ImageView
    lateinit var btnEthereum:ImageView
    lateinit var btnSolana:ImageView
    lateinit var btnAxie:ImageView
    lateinit var btnLitecoin:ImageView
    private lateinit var auth: FirebaseAuth
    lateinit var fireStore : FirebaseFirestore





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        val Rl_Main:RelativeLayout = findViewById(R.id.Rl_Main)
        cellChooseCoin = View.inflate(this,R.layout.cell_choose_coin,null)
        cellAddBoughtTransaction = View.inflate(this,R.layout.add_bought_transaction_details,null)
        cellAddSoldTransaction = View.inflate(this,R.layout.add_sold_transaction_details,null)
        init()
        Rl_Main.addView(cellChooseCoin)
        showCoinsName()


        //Choose coin
        BtnNext.setOnClickListener {
            Rl_Main.addView(cellAddBoughtTransaction)
        }
        //Add bought transaction
        BtnDoneBoughtTransaction.setOnClickListener {

            amountCoinBought = Et_AmountCoinBought.text.toString()
            priceCoinBought = Et_PriceCoinBought.text.toString()
            dateCoinBought = Et_dateCoinBought.text.toString()

            uploadTransactionBought(typeCoin,amountCoinBought,priceCoinBought,dateCoinBought)
            BtnDoneBoughtTransaction.isEnabled = false

        }





    }
    fun init(){

        //Initialize Widgets
        Et_AmountCoinBought = cellAddBoughtTransaction.findViewById(R.id.Et_AmountCoinBought)
        Et_AmountCoinSold = cellAddSoldTransaction.findViewById(R.id.Et_AmountCoinSold)
        Et_PriceCoinBought = cellAddBoughtTransaction.findViewById(R.id.Et_PriceCoinBought)
        Et_PriceCoinSold = cellAddSoldTransaction.findViewById(R.id.Et_PriceCoinSold)
        Et_dateCoinBought = cellAddBoughtTransaction.findViewById(R.id.Et_dateCoinBought)
        Et_dateCoinSold = cellAddSoldTransaction.findViewById(R.id.Et_dateCoinSold)

        BtnDoneBoughtTransaction = cellAddBoughtTransaction.findViewById(R.id.BtnDoneBoughtTransaction)
        BtnDoneSoldTransaction = cellAddSoldTransaction.findViewById(R.id.BtnDoneSoldTransaction)
        BtnNext = cellChooseCoin.findViewById(R.id.BtnNext)

        btnBitcoin = cellChooseCoin.findViewById(R.id.Ic_Bitcoin)
        btnEthereum = cellChooseCoin.findViewById(R.id.Ic_Ethereum)
        btnAxie = cellChooseCoin.findViewById(R.id.Ic_Axie)
        btnLitecoin = cellChooseCoin.findViewById(R.id.Ic_Litecoin)
        btnSolana = cellChooseCoin.findViewById(R.id.Ic_Solana)


        // Initialize Firebase Auth and Firestore
        auth = Firebase.auth
        fireStore = Firebase.firestore

    }

    private fun uploadTransactionBought(typeCoin:String,amountCoinBought:String,priceCoinBought:String,dateCoinBought:String){
        val userId = auth.uid
        val transactionBought = TransactionModel(typeCoin,amountCoinBought.toDouble(),priceCoinBought.toDouble(),dateCoinBought,userId.toString(),null,null,null)

        fireStore.collection("openTransactions").add(transactionBought)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
                finish()

            }
            .addOnFailureListener {
                    e -> Log.w(TAG, "Error writing document", e)
                Toast.makeText(this,"Something went wrong \n-_(-_-)_-",Toast.LENGTH_SHORT).show()
                BtnDoneBoughtTransaction.isEnabled = true
            }


    }
    fun showCoinsName(){

        btnBitcoin.setOnClickListener {
            makeToastShort(this,"Bitcoin")
            typeCoin = "Bitcoin"
        }
        btnSolana.setOnClickListener {
            makeToastShort(this,"Solana")
            typeCoin = "Solana"
        }
        btnAxie.setOnClickListener {
            makeToastShort(this,"Axie")
            typeCoin = "Axie"
        }
        btnLitecoin.setOnClickListener {
            makeToastShort(this,"Litecoin")
            typeCoin = "Litecoin"
        }
        btnEthereum.setOnClickListener {
            makeToastShort(this,"Ethereum")
            typeCoin = "Ethereum"
        }
    }



    public fun makeToastShort(context:Context,text:String){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }
}