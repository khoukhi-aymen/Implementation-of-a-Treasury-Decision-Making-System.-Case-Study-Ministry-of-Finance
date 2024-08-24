exports.isAuth = (req,res,next)=>{
    if(verifUser == "connexion"){
        next()
    }else{
        res.redirect('/login')
    }
}



exports.isNotAuth = (req,res,next)=>{
    if(verifUser == "connexion"){
        res.redirect('/')
    }else{
        next()
    }
}