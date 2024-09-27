package com.example.firebasechatapp.data

class User  {
    var name : String? = null
    var mail : String? = null
    var uid  : String? = null

    constructor()
    constructor( name : String? ,
                 mail : String?,
                 uid  : String?){
        this.name = name
        this.mail = mail
        this.uid = uid
    }

}