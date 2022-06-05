// import mongoose 
import mongoose from "mongoose";

// Buat Schema
const Product = mongoose.Schema({
    title:{
        type: String,
        required: true
    },
    image:{
        type: String,
        required: true
    },
    desc:{
        type: String,
        required: true
    },
    // author : { // opsional karena md yg buat api register and login
    //     type : Object
    //    require : true
    // }
}, {
    timestamps:true    

});

// export model
export default mongoose.model('Products', Product);