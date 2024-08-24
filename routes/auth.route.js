const route = require('express').Router()
const AuthController = require('../Controllers/auth.Contoller')
const body = require('express').urlencoded({urlencoded:true})
const GuardAuth = require('./guardAuth')


/******************************** register ***************************/

route.get('/register',AuthController.getRegisterPage)
route.post('/register', body, (req, res) => {
    const name = req.body.name;
    const email = req.body.email;
    const password = req.body.password;

    // Appeler la fonction pour envoyer les données au serveur Java
    AuthController.sendDataToServer(name, email, password)
        .then(msj => {
            console.log('msj', msj);
            
            // Redirection vers la page Connectez-vous si le message est vide
            if (msj === 'Email existe deja.....') {
                res.render('Connectez-vous', { msj:msj, role:'' }); // Passer msj au modèle
            } else {
                res.render('Connectez-vous',{ msj:msj, role:''});
            }
        })
        .catch(error => {
            console.error('Error:', error);
            // Gérer l'erreur ici
            res.status(500).send('Internal Server Error');
        });
});




/******************************** register ***************************/



/******************************** login ******************************/
route.get('/login',AuthController.getLoginPage)
route.post('/login',body,(req, res) => {
    const mail = req.body.mail;
    const psw = req.body.psw;
    // Appeler la fonction pour envoyer les données au serveur Java
    AuthController.sendDataLoginToServer(mail, psw)
    .then(role => {
        console.log('Role: ', role);
        // Utilisez le role de l'utilisateur comme vous le souhaitez dans votre application
        if(role === 'Admin front Office'){
          console.log("le role de l'utilisateur est: "+role)
          res.redirect('/dashbordFOFF');
        }else if(role === 'Admin Back Office'){
            console.log("le role de l'utilisateur est: "+role)
            res.redirect('/depenses');
        }else if(role === 'utilisateur financement'){
            console.log("le role de l'utilisateur est: "+role)
            res.redirect('/DettesReelUF');
        }else if(role === 'User Recettes'){
            console.log("le role de l'utilisateur est: "+role)
            res.redirect('/RecettesReelUR');
        }else if(role === 'User Depenses'){
            console.log("le role de l'utilisateur est: "+role)
            res.redirect('/depensesReelUD');
        }else if(role === 'User haut niveau'){
            console.log("le role de l'utilisateur est: "+role)
            res.redirect('/dashbordHN');
        }else if(role === 'Mot de passe incorrect'){
            console.log(role)
            res.render('Connectez-vous', { role:role ,msj:''}); // Passer msj+role au view
        }else if(role === 'Compte inexistant'){
            console.log(role)
            res.render('Connectez-vous', { role:role ,msj:''}); // Passer msj+role au view
        }else if(role === 'Role vide'){
            console.log(role)
            res.render('Connectez-vous', { role:role ,msj:''}); // Passer msj+role au view
        }

        
    })
    .catch(error => {
        console.error('Error:', error);
    });;

});



/******************************** login ******************************/



/******************************** logout ******************************/
route.post('/logout',AuthController.logoutFunctionController)

/******************************** logout ******************************/

module.exports=route