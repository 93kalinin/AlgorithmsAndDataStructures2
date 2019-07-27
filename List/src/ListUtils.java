class ListUtils {

    private ListUtils() {}    // this class should never be instantiated

    static void checkForNull(Object that, String message) {
        if (that == null)
            throw new IllegalArgumentException(message);
    }
}
