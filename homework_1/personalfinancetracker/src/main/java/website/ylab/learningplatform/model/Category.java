//package website.ylab.learningplatform.model;
//
//public class Category {
//    private Long id;
//    private String name;
//    private boolean expense;
//
//    // Default constructor needed for JDBC
//    public Category() {
//    }
//
//    public Category(String name, boolean expense) {
//        this.name = name;
//        this.expense = expense;
//    }
//
//    // Getters and Setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public boolean isExpense() {
//        return expense;
//    }
//
//    public void setExpense(boolean expense) {
//        this.expense = expense;
//    }
//
//    @Override
//    public String toString() {
//        return "Category{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", expense=" + expense +
//                '}';
//    }
//}
package website.ylab.learningplatform.model;

public enum Category {
    INCOME,
    GROCERIES,
    UTILITIES,
    RENT,
    ENTERTAINMENT,
    MEDICAL,
    TRANSPORTATION,
    EDUCATION,
    MISC
}
