package lld.wearehousemangement;

public class ProductFactory {


    public Product getProduct(ProductCategories categories,String sku,String name,Double price,int quantity){
        switch (categories){
            case RETAIL:
                return new RetailsProducts(name,price,categories,quantity,sku);

            case ELECTRONIC :
                return new ElectronicProducts(name,price,categories,quantity,sku, null, 0);

            default :
                throw  new IllegalArgumentException();
        }
    }
    
    public Product createProduct(String sku, String name) {
        return createProduct(sku, name, ProductCategories.RETAIL, 0.0, 1);
    }
    
    public Product createProduct(String sku, String name, ProductCategories category, double price, int quantity) {
        return getProduct(category, sku, name, price, quantity);
    }
}
