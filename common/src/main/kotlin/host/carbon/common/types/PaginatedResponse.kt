package host.carbon.common.types

data class PaginatedResponse<T>(
    val pagination: Pagination,
    val data: T
)
