package website.ylab.learningplatform.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testEnumValues() {
        // Test that the enum has the expected number of values
        assertEquals(9, Category.values().length);

        // Test that all expected values are present
        assertArrayEquals(
                new Category[]{
                        Category.INCOME,
                        Category.GROCERIES,
                        Category.UTILITIES,
                        Category.RENT,
                        Category.ENTERTAINMENT,
                        Category.MEDICAL,
                        Category.TRANSPORTATION,
                        Category.EDUCATION,
                        Category.MISC
                },
                Category.values()
        );
    }

    @Test
    void testEnumValuesOrder() {
        // Test the order of enum values
        Category[] values = Category.values();
        assertEquals(Category.INCOME, values[0]);
        assertEquals(Category.GROCERIES, values[1]);
        assertEquals(Category.UTILITIES, values[2]);
        assertEquals(Category.RENT, values[3]);
        assertEquals(Category.ENTERTAINMENT, values[4]);
        assertEquals(Category.MEDICAL, values[5]);
        assertEquals(Category.TRANSPORTATION, values[6]);
        assertEquals(Category.EDUCATION, values[7]);
        assertEquals(Category.MISC, values[8]);
    }

    @Test
    void testValueOf() {
        // Test that valueOf works correctly for each enum value
        assertEquals(Category.INCOME, Category.valueOf("INCOME"));
        assertEquals(Category.GROCERIES, Category.valueOf("GROCERIES"));
        assertEquals(Category.UTILITIES, Category.valueOf("UTILITIES"));
        assertEquals(Category.RENT, Category.valueOf("RENT"));
        assertEquals(Category.ENTERTAINMENT, Category.valueOf("ENTERTAINMENT"));
        assertEquals(Category.MEDICAL, Category.valueOf("MEDICAL"));
        assertEquals(Category.TRANSPORTATION, Category.valueOf("TRANSPORTATION"));
        assertEquals(Category.EDUCATION, Category.valueOf("EDUCATION"));
        assertEquals(Category.MISC, Category.valueOf("MISC"));
    }

    @Test
    void testInvalidValueOf() {
        // Test that valueOf throws exception for invalid value
        assertThrows(IllegalArgumentException.class, () -> Category.valueOf("INVALID_CATEGORY"));
    }

    @Test
    void testOrdinals() {
        // Test that ordinals are assigned correctly
        assertEquals(0, Category.INCOME.ordinal());
        assertEquals(1, Category.GROCERIES.ordinal());
        assertEquals(2, Category.UTILITIES.ordinal());
        assertEquals(3, Category.RENT.ordinal());
        assertEquals(4, Category.ENTERTAINMENT.ordinal());
        assertEquals(5, Category.MEDICAL.ordinal());
        assertEquals(6, Category.TRANSPORTATION.ordinal());
        assertEquals(7, Category.EDUCATION.ordinal());
        assertEquals(8, Category.MISC.ordinal());
    }
}