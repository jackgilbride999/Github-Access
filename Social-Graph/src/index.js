console.log('Hello Project.');

const MongoClient = require('mongodb').MongoClient;
const assert = require('assert');

// Connection URL
const uri = "mongodb+srv://readonly:AeDQC9AgUzsiBI07@github-ueksk.mongodb.net/test?retryWrites=true&w=majority";

// Database Name
const dbName = 'Github';

// Creating a new MongoClient
const client = new MongoClient(uri, { useNewUrlParser: true });

// Use connect method to connect to the server
// Use connect method to connect to the Server
client.connect(function(err) {
    assert.equal(null, err);
    console.log("Connected successfully to server");
  
    const db = client.db(dbName);
  
    findDocuments(db, function() {
        client.close();
      });  
    });
  

  const findDocuments = function(db, callback) {
    // Get the documents collection
    const collection = db.collection('repos');
    // Find some documents
    collection.find({}).toArray(function(err, docs) {
      assert.equal(err, null);
      console.log("Found the following records");
      console.log(docs)
      callback(docs);

    });

  }