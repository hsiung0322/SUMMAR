//import package
var mongodb = require('mongodb'); //引入mongodb
var mysql = require('mysql'); //引入mysql
var express = require('express');
var bodyparser = require('body-parser');
var bcrypt = require('bcrypt')
//var multer = require('multer') // v1.4.2
//var upload = multer() // for parsing multipart/form-data

//function 字首大寫
var fistLetterUpper = function (str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
};
var encryption = function(password){
    return bcrypt.hashSync(password,10); //加密
}
var decryption = function(password,hash){
    return bcrypt.compareSync(password,hash); //解密 true/false
}

//Create Express Service
var app = express();
app.use(bodyparser.json());
app.use(bodyparser.urlencoded({ extended: true }));

//建立 mysql 連線
var MysqlClient = mysql.createConnection({
    host: '127.0.0.1',
    user: 'root',
    database: 'summar'
});

// 建立連線後不論是否成功都會呼叫
MysqlClient.connect(function (err) {
    if (err)
        console.log('Unable to connect to the MySQL server.Error', err);
    else {
        app.listen(3030, () => {
            console.log('Connected to MySQL Server, WebServer running on port 3030');
        });
        //getUsers
        app.get('/users', (req, res, next) => {
            MysqlClient.query('SELECT * FROM `member`', function (err, result, fields) {
                if (err) {
                    console.log(err);
                    res.json({
                        status: false,
                        message: 'there are some error with query'
                    })
                }
                else {
                    res.json({
                        status: true,
                        data: result,
                        message: 'sucessfully'
                    });
                }
            });
        });
        //register
        app.post('/users/register', (req, res, next) => {
            var email = req.body.email;
            var password =req.body.password;
            var name = req.body.name;
            var gender = req.body.gender;
            var birth = req.body.birth;
            var data = {
                'email':email,
                'password':encryption(password),
                'name':name,
                'gender':gender,
                'birth':birth
            }
            console.log(email);
            //check exists
            MysqlClient.query('SELECT *  FROM `member` WHERE email = ?', email, function (err, results) {
                if (err) {
                    res.json({
                        status: false,
                        message: err.message
                    })
                } else {
                    var result = JSON.parse(JSON.stringify(results)); //[ RowDataPacket { count: 0 } ]
                    //console.log(count[0].count); //{ count: 0 }
                    //如果不等於0，表示已存在
                    if (result.length != 0) {
                        res.json({
                            status: false,
                            message: 'aleardy existed'
                        })
                    }
                    else {
                        MysqlClient.query('INSERT INTO `member` SET ?', data, function (err, results, fields) {
                            if (err) {
                                res.json({
                                    status: false,
                                    message: err.message
                                })
                            }
                            else {
                                res.json({
                                    status: true,
                                    data: results,
                                    message: 'sucessfully'
                                });
                            }
                        });
                    }
                }

            })
        })
        //login
        app.post('/users/login', (req, res, next) => {
            var email = req.body.email;
            var password =req.body.password; //使用者輸入的
            //check exists
            MysqlClient.query('SELECT * FROM `member` WHERE email = ?', email, function (err, results, fields) {
                var result = JSON.parse(JSON.stringify(results));
                if (err) {
                    res.json({
                        status: false,
                        message: err.message
                    })
                }
                else {
                    if (result.length > 0) { //有帳號
                        var psw =result[0].password;//mysql取得的密碼
                        if (decryption(password,psw)) {
                            res.json({
                                status: true,
                                message: 'sucessfully'
                            })
                        }
                        else {
                            res.json({
                                status: false,
                                message: 'Email or Password error'
                            })
                        }
                    }
                    else {
                        res.json({
                            status: false,
                            message: 'Email does not exist'
                        })
                    }
                }
            })
        })
    }
});


//Create MongoDB Client
var MongoClient = mongodb.MongoClient;

//autn
var auth = {
    'username': 'summar',
    'password': 'summar',
    'dbname': 'summar'
}
// var auth = {
//     'username':'admin',
//     'password':'123456',
//     'dbname':'admin'
// }

//Connection URL
const url = 'mongodb://' + auth.username + ':' + auth.password + '@163.13.202.209:27017/' + auth.dbname;

MongoClient.connect(url, { useNewUrlParser: true }, function (err, client) {
    if (err)
        console.log('Unable to connect to the mongoDB server.Error', err);
    else {
        const db = client.db('summar'); //db
        const table = db.collection('summar') //資料表table

        //start Web Server
        app.listen(3000, () => {
            console.log('Connected to MongoDB Server, WebServer running on port 3000');
        });
        //Register
        app.post('/register', (request, response, next) => {
            var post_data = request.body;

            var password = post_data.password;
            var name = post_data.name;
            var email = post_data.email;

            var insertjson = {
                'email': email,
                'name': name,
                'password': password
            };
            var db = client.db('dev_test');

            //check exists
            db.collection('user')
                .find({ 'email': email }).count(function (err, number) {
                    if (number != 0) {
                        response.json('Email aleardy exists');
                        console.log('Email aleardy exists');
                    }
                    else {
                        //Insert data
                        db.collection('user')
                            .insertOne(insertjson, function (error, res) {
                                response.json('Successful!');
                                console.log('Successful!');
                            })
                    }
                })
        });
        //get news
        app.get('/news', (req, res, next) => {

            const page = parseInt(req.query.page)
            const perpage = parseInt(req.query.perpage) //size
            // const perpage = 30;

            const startIndex = (page - 1) * perpage
            const endIndex = perpage

            //query
            table.find({}).count(function (err, number) {
                var endPage = Math.ceil(number / perpage);
                if (number == 0) {
                    res.json('No data');
                    console.log('No data');
                }
                else {
                    db.collection('summar').find({}).sort({ date: -1 }).skip(startIndex).limit(endIndex).toArray(function (err, result) {
                        if (err) {
                            res.json(err);
                            console.log(err);
                        }
                        if (page > endPage) {
                            res.json("No data 沒資料");
                            console.log("No data 沒資料");
                        }
                        else {
                            res.json(result);
                            console.log(result);
                        }
                    });
                }
            })
        });
        //get news by getegory
        app.get('/news/cal/:categoryName', (request, response, next) => {
            var categoryName = fistLetterUpper(request.params.categoryName);
            console.log(categoryName);

            //check exists
            table.find({ 'classification': categoryName }).count(function (err, number) {
                if (number == 0) {
                    response.json('No data-cal');
                    console.log('No data');
                }
                else {
                    db.collection('summar').find({ 'classification': categoryName }).sort({ date: -1 }).toArray(function (err, result) {
                        if (err) {
                            response.json(err);
                            console.log(err);
                        }
                        else {
                            response.json(result);
                            console.log(result);
                        }
                    });
                }
            })
        });
        //get news/title
        app.get('/news/title', (req, res, next) => {

            const title = req.query.title

            const page = parseInt(req.query.page)
            const perpage = parseInt(req.query.perpage) //size

            const startIndex = (page - 1) * perpage
            const endIndex = perpage

            table.find({}).count(function (err, number) {
                var endPage = Math.ceil(number / perpage);
                if (number == 0) {
                    res.json('No data');
                    console.log('No data');
                }
                else {
                    db.collection('summar').find({ title: { $regex: title, $options: "i" } }).sort({ date: -1 }).skip(startIndex).limit(endIndex).toArray(function (err, result) {
                        if (err) {
                            res.json(err);
                            console.log(err);
                        }
                        if (page > endPage) {
                            res.json("No data 沒資料");
                            console.log("No data 沒資料");
                        }
                        else {
                            res.json(result);
                            console.log(result);
                        }
                    });
                }
            })
        });
        app.get('/catalogList', (req, res, next) => {
            const table = db.collection('catalog') //資料表table
            table.find({}).count(function (err, number) {
                if (number == 0) {
                    res.json(number);
                    console.log('No data');
                }
                else {
                    table.find({}).toArray(function (err, result) {
                        if (err) {
                            res.json(err);
                            console.log(err);
                        }
                        else {
                            res.json(result);
                            console.log(result);
                        }
                    });
                }
            })
        });
    }
});
