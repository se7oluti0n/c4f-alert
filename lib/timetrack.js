var qs = require('querystring');

exports.sendHtml = function(res, html) { 
  res.setHeader('Content-Type', 'text/html');
  res.setHeader('Content-Length', Buffer.byteLength(html));
  res.end(html);
}

exports.parseReceivedData = function(req, cb) { 
  var body = '';
  req.setEncoding('utf8');
  req.on('data', function(chunk){ body += chunk });
  req.on('end', function() {
    var data = qs.parse(body);
    cb(data);
  });
};

exports.actionForm = function(id, path, label) { 
  var html = '<form method="POST" action="' + path + '">' +
    '<input type="hidden" name="id" value="' + id + '">' +
    '<input type="submit" value="' + label + '" />' +
    '</form>';
  return html;
};

exports.add = function(db, req, res) {
  exports.parseReceivedData(req, function(work) { 
    db.query(
      "INSERT INTO custom_disaster (dtype, lat, lon, radius, time, description, last_time) " + 
      " VALUES (?, ?, ?, ?, ?, ?, now())",
      [work.dtype, work.lat, work.lon, work.radius, work.time, work.description], 
      function(err) {
        if (err) throw err;
        exports.show(db, res); 
      }
    );
  });
};



exports.delete = function(db, req, res) {
  exports.parseReceivedData(req, function(work) { 
    db.query(
      "DELETE FROM custom_disaster WHERE id=?", 
      [work.id], 
      function(err) {
        if (err) throw err;
        exports.show(db, res); 
      }
    );
  });
};

exports.archive = function(db, req, res) {
  exports.parseReceivedData(req, function(work) { 
    db.query(
      "UPDATE custom_disaster SET archived=1 WHERE id=?", 
      [work.id], 
      function(err) {
        if (err) throw err;
        exports.show(db, res); 
      }
    );
  });
};

exports.register = function(db, req, res){
  exports.parseReceivedData(req, function(work) {
	db.query(
		"SELECT id FROM accounts WHERE username=?",
		[work.username],
		function(err, rows) {
			if (err) throw err;
			if (rows.length>0)
			{
				res.writeHead(200, {"Content-Type": "application/json"});
				var json = JSON.stringify({
				  	username    : work.username,
					description : "username exists!"
				});				
				res.end(json);
			}
			else {
			    db.query(
				  "INSERT INTO accounts (username) " + 
				  "VALUES (?)",
				  [work.username], 
				  function(err) {
					if (err) throw err;
					res.writeHead(200, {"Content-Type": "application/json"});
					var json = JSON.stringify ({
						username : work.username,
						description : "Register success"
					});
					res.end(json);
				  }
				);
			}
		}
	);
  });
};

exports.login = function(db, req, res) {
	exports.parseReceivedData(req, function(work){
		db.query(
			"SELECT username, password, active FROM accounts WHERE username=?",
			[work.username],
			function (err, rows){
				if (err) throw err;	
				if (rows.length<1) { // username not found
					res.writeHead(200, {"Content-Type" : "application/json"});
					var json = JSON.stringify ({
						username : work.username,
						description : "username not found"
					});
					res.end(json);
				} else if (rows[0].password!=work.password) { // wrong password
					var json = JSON.stringify({
						username : work.username,
						description : "wrong password"
					});
					res.end(json);
				} else if (rows[0].active==1) {
					res.writeHead(200, {"Content-Type" : "application/json"});
					var json = JSON.stringify({
						username : work.username,
						description : "Already log in"
					});
					res.end(json);
				} else {
					db.query(
						"UPDATE accounts SET active=? WHERE username=?",
						[1, work.username],
						function(err) {
							if (err) throw err;
							var json = JSON.stringify({
								username : work.username,
								description : "login success"
							});
							res.end(json);
						}
					);
				}
			}
		);
	});
}

exports.logout = function (db, req, res) {
	exports.parseReceivedData(req, function(work){
		db.query (
			"SELECT active FROM accounts WHERE username=?",
			[work.username],
			function (err, rows) {
				if (rows[0].active==0) {
					res.writeHead(200, {"Content-Type" : "application/json"});
					var json = JSON.stringify({
						username : work.username,
						description : "Had logged out"
					});
					res.end(json);
				} else {
					db.query(
						"UPDATE accounts SET active=? WHERE username=?",
						[0, work.username],
						function(err) {
							if (err) throw err;
							var json = JSON.stringify({
								username : work.username,
								description : "Log out success"
							});
							res.end(json);
						}
					);
				}
			}
		);
	});	
}

exports.update = function(db, req, res){
	exports.parseReceivedData(req, function(work){
		console.log(work);
		db.query(
			"UPDATE accounts SET lat=?, lon=?, city= ? WHERE username=?",
			[work.lat, work.lon, work.city, work.username],
			function (err){
				if (err) throw err;	
				res.writeHead(200, {"Content-Type": "application/json"});
				var json = JSON.stringify({
				  	lat : work.lat,
				  	lon : work.lon	
				});
  				res.end(json);
			}	
		);

	//console.log(req);

	});

};


exports.badluck_user = function(db, city, to){

	console.log(city);
	db.query(
		"SELECT last_time FROM cities WHERE city= ?",
		[city],
		function(err, rows){
			if (err) throw err;

			var last_update_time = rows[0].last_time;
			db.query(
				"UPDATE accounts SET status=1 WHERE  city = ? AND last_time < ?",
				[city, last_update_time],
				function (errr)
				{
					if (errr) throw errr;
				}
			);

		}
	);

	console.log('update xong');
	to[city] = 0;

}

exports.confirm_info = function(db, req, res, time_out){
	exports.parseReceivedData(req, function(work){
		console.log(work);



		 if ( ! time_out[work.city]){
		 	time_out[work.city] = 1;
		 	db.query(
			"SELECT city FROM cities WHERE city = ?",
			[work.city],
			function (err, rows) {
				if (err) throw err;

				if (rows.length > 0)
				{
					db.query(
						"UPDATE cities SET IsDanger=1, last_time=now() WHERE city=?",
						[work.city],
						function (err){
							if (err) throw err;
					});
				}
				else
				{
					db.query(
						"INSERT INTO cities(city, IsDanger, last_time)" +
						"VALUES(?, 1, now())",
						[work.city],
						function(err){
							if (err) throw err;
						}
					);
				}

			}
		);
		 	setTimeout(function(){exports.badluck_user(db, work.city, time_out)}, 30000);
		}

		db.query(
			"UPDATE accounts SET status=?, lat=?, lon=?, city= ?, last_time=now() WHERE username=?",
			[work.status, work.lat, work.lon, work.city, work.username],
			function (err){
				if (err) throw err;	
				//res.writeHead(200, {"Content-Type": "application/json"});
				//var json = JSON.stringify({
				//  	lat : work.lat,
				//  	lon : work.lon	
				//});
  				//res.end(json);
  				res.writeHead(200);
  				res.end('Goodluck nextime');
			}	
		);



		// if (work.status == 1){
		// 	db.query(
		// 		"UPDATE accounts SET help_id = ? WHERE username <> ?",
		// 	[work.username, work.username],
		// 	function (err){
		// 		if (err) throw err;	
		// 		//res.writeHead(200, {"Content-Type": "application/json"});
		// 		//var json = JSON.stringify({
		// 		//  	lat : work.lat,
		// 		//  	lon : work.lon	
		// 		//});
  // 				//res.end(json);
		// 	});

		// }

	});
};


exports.notify = function(db, req, res)
{
	exports.parseReceivedData(req, function (work){
		db.query(
		"SELECT * FROM custom_disaster WHERE DATEDIFF(now(), last_time) < 5  ORDER BY last_time DESC",

		function(err, rows){
			if (err) throw err;

			for (var i = 0; i < rows.length; i++){

				if ( exports.calculateGPSDistance(work.lat, work.lon, rows[i]. lat, rows[i].lon) < 100)
				{
					res.writeHead(200, {"Content-Type": "application/json"});
					var json = JSON.stringify(rows[i]);
	  				res.end(json);	
					break;
				}
			}
		}	
		);
	});
}

exports.help_result = function(db, req, res)
{
	exports.parseReceivedData(req, function (work){
		db.query(
			"UPDATE accounts SET status=? WHERE username=?",
			[work.status, work.victim],
			function (err)
			{
				if (err) throw err;

				res.writeHead(200);
				res.end('Thank you');
			}


		);

	});

}

exports.help_me = function (db, req, res){
		exports.parseReceivedData(req, function (work){
			db.query(
				"UPDATE accounts SET lat=?, lon=?, city=?, status=1, last_time = now() WHERE username =  ?",
				[work.lat, work.lon, work.city, work.username],
				function (err){
					if (err) throw err;

					res.writeHead(200);
  					res.end("It's will be ok");
				}
			);
		});		
};




exports.calculateGPSDistance = function (lat1, lon1, lat2, lon2){
	var R = 6371; // km

	var toRad = Math.PI / 180;
	var dLat = (lat2-lat1) * toRad;
	var dLon = (lon2-lon1) * toRad;
	var lat1 = lat1 * toRad;
	var lat2 = lat2 * toRad;

	//console.log(lat1," ", lat2, " ",  dLat, " ", dLon);

	var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
	var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
	var d = R * c;

	console.log('distance: ', d);
	return Math.abs(d);
}


exports.help_friends = function(db, req, res)
{
	exports.parseReceivedData(req, function (work){
		// db.query(
		// 	"SELECT username, lat, lon, status",
		// 	[],
		// 	function(err, rows)
		// 	{
		// 		//if (err) throws err;


		// 	}
		// );

	});
}
exports.help_others = function (db, req, res){
	exports.parseReceivedData(req, function (work){

		console.log(work);
		db.query(
			"SELECT username, lat, lon, city, message FROM accounts WHERE status = 1",
			function(err, rows){
				if (err) throw err;
				
				// if (rows[0].help_id){
				// 	res.writeHead(200, {"Content-Type": "application/json"});
				// 	var json = JSON.stringify({
				// 	username : rows[0].help_id,
				//   	lat : work.lat,
				//   	lon : work.lon, 
				//   	message: rows[0].message	
				// }); 				
  		// 		res.end(json);	
				// } 

				var weak = [];

				console.log(rows);
				

				for (var i = 0; i < rows.length; i++)
				{
					if (work.distance > exports.calculateGPSDistance(work.lat, work.lon, rows[i].lat, rows[i].lon)){
						console.log(rows[i]);	
						weak.push(rows[i]);
						 
					}
				}
				res.writeHead(200, {"Content-Type": "application/json"});
				var json = '{"helper_array" :[';

				for (var i = 0; i < weak.length - 1; i++)
				
					json += JSON.stringify(weak.pop()) + ',';

				json += JSON.stringify(weak.pop()) + ']}';

				console.log("Result for ", work.lat, " ", json);
				res.end(json);
			}
		);
	});
}

exports.conclude_disaster = function(db, id)
{
	db.query(
		"SELECT COUNT(uid) FROM confirm_disaster WHERE did=?",
		[id], 
		function(err, rows){
			if (err) throw err;
			console.log(rows);
	});
}
exports.found_disaster = function(db, req, res)
{
	exports.parseReceivedData(req, function (work){
		db.query(
			"INSERT INTO user_disaster(type, lat, lon, distance, username, discription, confirm, last_time ) "
			+ "VALUES(?, ?, ?, ?, ?, ?, 0, now())",
			[work.type, work.lat, work.lon, work.distance, work.username, work.discription],
			function( err){
				if (err) throw err;

				res.writeHead(200);
				res.end('Thank you for your info');


				db.query(
					"SELECT id FROM user_disaster ORDER BY last_time DESC",
					function(errr, rows){
						if (errr) throw errr;

						setTimeout(function(){
							exports.conclude_disaster(db, rows[0].id);
						}, 20000);
				});
			});
	});
}

exports.request_confirm_disaster = function( db, req, res)
{
	exports.parseReceivedData(req, function (work){

		console.log(work);
		db.query(
			"SELECT * FROM user_disaster",
			function(err, rows){
				if (err) throw err;
				
				// if (rows[0].help_id){
				// 	res.writeHead(200, {"Content-Type": "application/json"});
				// 	var json = JSON.stringify({
				// 	username : rows[0].help_id,
				//   	lat : work.lat,
				//   	lon : work.lon, 
				//   	message: rows[0].message	
				// }); 				
  		// 		res.end(json);	
				// } 

				var weak = [];

				console.log(rows);
				

				for (var i = 0; i < rows.length-1; i++)
				{
					if (rows[i].distance * 2 > exports.calculateGPSDistance(work.lat, work.lon, rows[i].lat, rows[i].lon)){
						console.log(rows[i]);	
						weak.push(rows[i]);
						 
					}
				}
				res.writeHead(200, {"Content-Type": "application/json"});
				var json = '{"disaster_array" :[';

				for (var i = 0; i < weak.length - 1; i++)
				
					json += JSON.stringify(weak.pop()) + ',';

				json += JSON.stringify(weak.pop()) + ']}';

				res.end(json);
			}
		);
	});
}


exports.confirm_disaster = function(db, req, res)
{

	exports.parseReceivedData(req, function (work){
		if (work.confirm == 1)
		{
			db.query(
				"INSERT INTO confirm_disaster (did, uid, last_time) " +
				"VALUES(?, ?, now())",
				[work.disaster, work.username],
				function(err){
					if (err) throw err;

				}




			);
		}	
		res.writeHead(200);
		res.end("Thank you for your answer");	

	});


}


exports.show = function(db, res, showArchived) {
  var query = "SELECT * FROM custom_disaster " + 
    "WHERE archived=? " +
    "ORDER BY last_time DESC";
  var archiveValue = (showArchived) ? 1 : 0;
  db.query(
    query,
    [archiveValue], 
    function(err, rows) {
      if (err) throw err;
      html = (showArchived)
        ? ''
        : '<a href="/archived">Archived Work</a><br/>';
      html += exports.workHitlistHtml(rows); 
      html += exports.workFormHtml();
      exports.sendHtml(res, html); 
    }
  );
};

exports.showArchived = function(db, res) {
  exports.show(db, res, true); 
};

exports.workHitlistHtml = function(rows) {
  var html = '<table>';
  for(var i in rows) { 
    html += '<tr>';
    html += '<td>' + rows[i].dtype + '</td>';
    html += '<td>' + rows[i].lat + '</td>';
    html += '<td>' + rows[i].lon + '</td>';
    html += '<td>' + rows[i].radius + '</td>';
    html += '<td>' + rows[i].time + '</td>';
    html += '<td>' + rows[i].description + '</td>';
    if (!rows[i].archived) { 
      html += '<td>' + exports.workArchiveForm(rows[i].id) + '</td>';
    }
    html += '<td>' + exports.workDeleteForm(rows[i].id) + '</td>';
    html += '</tr>';
  }
  html += '</table>';
  return html;
};

exports.workFormHtml = function() {
  var html = '<form method="POST" action="/">' + 
    '<p>Loai tham hoa:<br/><input name="dtype" type="text"><p/>' +
    '<p>Lat:<br/><input name="lat" type="text"><p/>' +
    '<p>Lon:<br/><input name="lon" type="text"><p/>' +
 //   '<p>City:<br/><input name="city" type=""><p/>' +
    '<p>Radius:<br/><input name="radius" type="text"><p/>' +
    '<p>Thoi gian:<br/><input name="time" type="text"><p/>' +
    
    '<p>Description:<br/>' +
    '<textarea name="description"></textarea></p>' +
    '<input type="submit" value="Add" />' +
    '</form>';
  return html;
};

exports.workArchiveForm = function(id) { 
  return exports.actionForm(id, '/archive', 'Archive');
}

exports.workDeleteForm = function(id) { 
  return exports.actionForm(id, '/delete', 'Delete');
}
