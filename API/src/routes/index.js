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
router.get('/', getProducts);
// Route get single
router.get('/:id', getProductById);
// Route CREATE
router.post('/', saveProduct);
// Route UPDATE
router.patch('/:id', updateProduct);
// Route DELETE
router.delete('/:id', deleteProduct);

// export router
export default router;

