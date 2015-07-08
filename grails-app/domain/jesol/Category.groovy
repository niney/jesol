package jesol

class Category {

    long category
    String title

    Date dateCreated
    Date lastUpdated

    static constraints = {
        category(nullable: true)
    }
}
