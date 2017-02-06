package com.academiaexpresssystem.Data

import java.util.ArrayList
import java.util.Calendar

class Order(var userName: String?, var userPhone: String?, var time: String?, var location: String?,
            var ingredients: ArrayList<String>?, var isStatus: Boolean, var position: String?, var id: String, var price: Int, var calendar: Calendar?)
