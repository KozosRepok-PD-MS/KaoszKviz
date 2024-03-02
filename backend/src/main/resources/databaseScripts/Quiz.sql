
create or alter function findQuizByOwnerId
(
    @ownerId bigint
)
returns table as
    return select * from quiz where owner_id = @ownerId
