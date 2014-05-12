var http = require('http');
var work = require('./lib/timetrack');
var mysql = require('mysql');
var express = require('express');
var app = new express();


var db = mysql.createConnection({
  host:     '127.0.0.1',
  user:     'root',
  password: '1234',
  database: 'disaster'
});


var next_time_out = [];

app.post('/', function(req, res){ work.add(db, req, res);});
app.post('/archive', function(req, res){ work.archive(db, req, res);});
app.post('/delete', function(req, res){ work.delete(db, req, res);});
app.post('/update', function(req, res){ work.update(db, req, res);});
app.post('/register', function(req, res){ work.register(db, req, res);});
app.post('/login', function(req, res){ work.login(db, req, res);});
app.post('/logout', function(req, res){ work.logout(db, req, res);});
app.post('/help_me', function(req, res){ work.help_me(db, req, res);});
app.post('/help_others', function(req, res){ work.help_others(db, req, res);});
app.post('/confirm_info', function(req, res){ work.confirm_info(db, req, res, next_time_out);});
app.post('/notify', function(req, res){ work.notify(db, req, res);});
app.post('/found_disaster', function(req, res){ work.found_disaster(db, req, res);});
app.post('/confirm_disaster', function(req, res){ work.confirm_disaster(db, req, res);});
app.post('/request_confirm_disaster', function(req, res){ work.request_confirm_disaster(db, req, res);});

app.get('/', function(req, res){ work.show(db, res);});
app.get('/archived', function(req, res){ work.showArchived(db, res);});

// var server = http.createServer(function(req, res) {
//   switch (req.method) {
//     case 'POST': 
//       switch(req.url) {
//         case '/':
//           work.add(db, req, res);
//           break;
//         case '/archive':
//           work.archive(db, req, res);
//           break;
//         case '/delete':
//           work.delete(db, req, res);
//           break;
//         case '/update':
//           //console.log('update command');
//           work.update(db, req, res);
//           break;
//         case '/register':
//           work.register(db, req, res);
//           break;

//         case '/login':
//           work.login(db, req, res);
//           break;

//         case '/logout':
//           work.logout(db, req, res); 
//           break;

//         case '/request_friend':

//           break;

//         case '/response_friend_request':

//           break;

//         case '/confirm_info':
//           work.confirm_info(db, req, res, next_time_out);

//           break;

//         case '/help_me':
//           work.help_me(db, req, res);

//           break;

//         case '/help_friends':
//           work.help_friends(db, req, res);

//         case '/help_result':
//           work.help_result(db, req, res);

//           break;
//         case '/found_disaster':
//           work.found_disaster(db, req, res);

//           break;

//         case '/request_confirm_disaster':
//           work.request_confirm_user_info(db, req, res);
//           break;

//         case '/confirm_disaster' :

//           work.confirm_disaster(db, req, res);
//           break;


//         case '/help_others':
//           work.help_others(db, req, res);
//           break;    

//         case '/notify':
//           work.notify(db, req, res);
//           break;
//       }
//       break;
//     case 'GET': 
//       switch(req.url) {
//         case '/':
//           console.log('root link');
//           work.show(db, res);
//           break;
//         case '/archived':
//           work.showArchived(db, res);
//           break;

//         case '/update':
//           console.log('update command');
//           work.update(db, req);
//           break;  

//       }
//       break;
//   }
// });

db.query(
  "CREATE TABLE IF NOT EXISTS work (" 
  + "id INT(10) NOT NULL AUTO_INCREMENT, " 
  + "hours DECIMAL(5,2) DEFAULT 0, " 
  + "date DATE, " 
  + "archived INT(1) DEFAULT 0, " 
  + "description LONGTEXT,"
  + "PRIMARY KEY(id))",
  function(err) { 
    if (err) throw err;
    console.log('Server started...');
    // server.listen(80, function(){
    //   console.log('server is listening');
    // });

    app.listen(80); 
  }
);
