const express = require('express')
const path = require('path')
const app = express()
const RouterAuth = require("./routes/auth.route")
const RouterAdminFrontOffice = require("./routes/AdminFrontOffice.route")
const RouterAdminBackOffice = require("./routes/AdminBackOffice.route")
const RouterUserFinancement = require("./routes/UserFinancement.route")
const RouterUserDepenses = require("./routes/UserDepenses.route")
const RouterUserRecettes = require("./routes/UserRecettes.route")
const RouterUserHautNiveau = require("./routes/UserHautNiveau.route")
const oracleDb = require('oracledb');
const GuardAuth = require('./routes/guardAuth')
const flash = require('connect-flash')


// Utilisez les middlewares express intégrés pour parser les requêtes JSON et les données de formulaire
app.use(express.json());
app.use(express.urlencoded({ extended: true }));



//middlwares
app.set('view engine','ejs')  // importer EJS
app.set('views','Views') // mettre views static folder
app.use(express.static(path.join(__dirname,'assets'))) // mettre assets static folder


app.use('/',RouterAuth)
app.use('/',RouterAdminFrontOffice)
app.use('/',RouterAdminBackOffice)
app.use('/',RouterUserFinancement)
app.use('/',RouterUserDepenses)
app.use('/',RouterUserRecettes)
app.use('/',RouterUserHautNiveau)








app.listen(3000,()=>{
    console.log('server run on port 3000......')
})