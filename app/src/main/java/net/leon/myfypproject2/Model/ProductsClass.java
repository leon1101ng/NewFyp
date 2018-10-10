package net.leon.myfypproject2.Model;

public class ProductsClass {
    private String ProductsCategorys,ProductsDescription,ProductsImage,ProductsName,ProductsPrice,ProductsQuantity;

    public ProductsClass(String productsCategorys, String productsDescription, String productsImage, String productsName, String productsPrice, String productsQuantity) {
        ProductsCategorys = productsCategorys;
        ProductsDescription = productsDescription;
        ProductsImage = productsImage;
        ProductsName = productsName;
        ProductsPrice = productsPrice;
        ProductsQuantity = productsQuantity;
    }

    public String getProductsCategorys() {
        return ProductsCategorys;
    }

    public void setProductsCategorys(String productsCategorys) {
        ProductsCategorys = productsCategorys;
    }

    public String getProductsDescription() {
        return ProductsDescription;
    }

    public void setProductsDescription(String productsDescription) {
        ProductsDescription = productsDescription;
    }

    public String getProductsImage() {
        return ProductsImage;
    }

    public void setProductsImage(String productsImage) {
        ProductsImage = productsImage;
    }

    public String getProductsName() {
        return ProductsName;
    }

    public void setProductsName(String productsName) {
        ProductsName = productsName;
    }

    public String getProductsPrice() {
        return ProductsPrice;
    }

    public void setProductsPrice(String productsPrice) {
        ProductsPrice = productsPrice;
    }

    public String getProductsQuantity() {
        return ProductsQuantity;
    }

    public void setProductsQuantity(String productsQuantity) {
        ProductsQuantity = productsQuantity;
    }

    public ProductsClass(){}


}
