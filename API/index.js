//import express
import express from "express";
//import mongoose
import mongoose from "mongoose";
// import routes
import route from "./src/routes/index.js";
//import cors
import cors from "cors";
//import multer 
import multer from "multer";

// construct express function
const app = express();


// upload image
const fileStorage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'images');
    },
    //make format extension file images
    filename:(req, file, cb) => {
        cb(null, new Date(). getTime() + '-' + file.originalname)
    }
})

const fileFilter = (req, file, cb) => {
    if(
        file.mimetype === 'image/png' || 
        file.mimetype === 'image/jpg' ||
        file.mimetype ==='image/jpeg' ) 
    {
       cb(null, true); 
    }else{
        cb(null, false);
    }
}
// connect ke database mongoDB
mongoose.connect("mongodb://localhost:27017/restful_db",{ 
    useNewUrlParser: true,
    useUnifiedTopology: true
});

// mongoose.connect("mongodb+srv://bernatd:24nPjNaGsx8kiGat@mycluster.dhuw6.mongodb.net/?retryWrites=true&w=majority",{
//     useNewUrlParser:true,
//     useUnifiedTopology: true
// });
const db = mongoose.connection;
db.on('error', (error)=> console.error(error));
db.once('open', () => console.log('Database Connected'));

// middleware 
app.use(cors());
app.use(express.json());
app.use('/v1/stories/',multer({storage: fileStorage, fileFilter: fileFilter}).single('image'),route);


// listening to port
app.listen('3000',()=> console.log('Server Running at port: 3000'));
