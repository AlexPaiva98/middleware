package middleware.lifecycle;

public enum Strategy {
    STATIC_INSTANCE,
    OPTIMIZED_STATIC_INSTANCE,
    PER_REQUEST_INSTANCE,
    OPTIMIZED_PER_REQUEST_INSTANCE,
}