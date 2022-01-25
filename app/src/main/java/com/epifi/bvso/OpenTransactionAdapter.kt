package com.epifi.bvso

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.epifi.bvso.models.TransactionModel
import drewcarlson.coingecko.CoinGeckoClient
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.ArrayList

class OpenTransactionAdapter(var transactionsList:List<TransactionModel>, val clickListener: (TransactionModel) -> Unit):RecyclerView.Adapter<OpenTransactionAdapter
.OpenTransactionViewHolder>() {


    public class OpenTransactionViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
          fun bind(openTransactionModel: TransactionModel,clickListener: (TransactionModel) -> Unit){
                val typeCoinIv:ImageView = itemView.findViewById<ImageView>(R.id.Iv_Coin)
                val amountCoinBoughtTv = itemView.findViewById<TextView>(R.id.Tv_amount_coin_bought)


            when{


                openTransactionModel.typeCoin.toString()=="Bitcoin"->{
                    typeCoinIv.setBackgroundResource(R.drawable.ic_bitcoin)




                    amountCoinBoughtTv.text = openTransactionModel.amountCoinBought.toString()+" Btc"
                }
                openTransactionModel.typeCoin.toString()=="Solana"->{
                    typeCoinIv.setBackgroundResource(R.drawable.ic_solana)
                     //coinGecko.getPrice("Solana","usd",false,false,false,false)
                    amountCoinBoughtTv.text = openTransactionModel.amountCoinBought.toString()+" Sol"
                }
                openTransactionModel.typeCoin.toString()=="Ethereum"->{
                    typeCoinIv.setBackgroundResource(R.drawable.ic_ethereum)
                    //coinGecko.getPrice("Solana","usd",false,false,false,false)

                    amountCoinBoughtTv.text = openTransactionModel.amountCoinBought.toString()+" Eth"
                }
                openTransactionModel.typeCoin.toString()=="Axie"->{
                    typeCoinIv.setBackgroundResource(R.drawable.ic_axie)
                    //coinGecko.getPrice("Solana","usd",false,false,false,false)

                    amountCoinBoughtTv.text = openTransactionModel.amountCoinBought.toString()+" Axie"
                }
                openTransactionModel.typeCoin.toString()=="Litecoin"->{
                    typeCoinIv.setBackgroundResource(R.drawable.ic_litecoin)
                    //coinGecko.getPrice("Solana","usd",false,false,false,false)

                    amountCoinBoughtTv.text = openTransactionModel.amountCoinBought.toString()+" Ltc"
                }




            }
              itemView.setOnClickListener {
                clickListener(openTransactionModel)
              }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenTransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cell_transaction,parent,false)
        return OpenTransactionViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: OpenTransactionViewHolder, position: Int) {
            holder.bind(transactionsList[position],clickListener)



    }
    override fun getItemCount(): Int {
        return transactionsList.size
    }


}