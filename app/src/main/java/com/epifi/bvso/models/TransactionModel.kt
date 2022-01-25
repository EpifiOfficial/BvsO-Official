package com.epifi.bvso.models

data class TransactionModel(
    val typeCoin:String?=null,
    val amountCoinBought:Double?=null,
    val priceCoinBought:Double?=null,
    val dateCoinBought:String?=null,
    val uID:String?=null,
    val amountCoinSold:Int?=null,
    val priceCoinSold:Int?=null,
    val dateCoinSold:String?=null
)
