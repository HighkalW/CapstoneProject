// import express
import express from "express";

// import controllers
import { getProducts, 
    getProductById, 
    saveProduct, 
    updateProduct,
    deleteProduct } from "../controllers/productController.js";

    // express router
const router = express.Router();

// Route get All 
router.get('/stories', getProducts);
// Route get single
router.get('/stories/:id', getProductById);
// Route CREATE
router.post('/stories', saveProduct);
// Route UPDATE
router.patch('/stories/:id', updateProduct);
// Route DELETE
router.delete('/stories/:id', deleteProduct);

// export router
export default router;

