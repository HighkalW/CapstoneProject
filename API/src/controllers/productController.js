
// import models
import Product from "../models/Product.js";

import path from "path";


// function get All
export const getProducts = async (req, res) => {
    try {
        const products = await Product.find();
        res.json(products);
    } catch (error) {
        res.status(500).json({message: error.message});
    }
    
}

// function get single
export const getProductById = async (req, res) => {
    try {
        const product = await Product.findById(req.params.id);
        res.json(product);
    } catch (error) {
        res.status(404).json({message: error.message});
    }
    
}



// function Create
export const saveProduct = async (req, res) => {
    // new product is a name file your model
    console.log(req.file);
    console.log(req.body);
    const product = new Product({
         title : req.body.title,
         desc : req.body.desc,
         image : req.file.path
    });
    try {
        const savedProduct = await product.save();
        res.status(201).json({
            message: "Stories fetched successfully",
            data: savedProduct
        });
    } catch (error) {
        res.status(400).json({message: error.message});
    }
}

// response when create a post story


// function Update
export const updateProduct = async (req, res) => {
    const cekId = await Product.findById(req.params.id);
    if(!cekId) return res.status(404).json({message: "Data tidak ditemukan"}); 
    try {
        const updatedProduct = await Product.updateOne({_id: req.params.id}, {$set: req.body});
        res.status(200).json(updatedProduct);
    } catch (error) {
        res.status(400).json({message: error.message});
    }
}

// function Delete
export const deleteProduct = async (req, res) => {
    const cekId = await Product.findById(req.params.id);
    if(!cekId) return res.status(404).json({message: "Data tidak ditemukan"});
    try {
        const deletedProduct = await Product.deleteOne({_id: req.params.id});
        res.status(200).json(deletedProduct);
    } catch (error) {
        res.status(400).json({message: error.message});
    }
}