
create or alter function findQuizByOwnerId
(
    @ownerId bigint
)
returns table as
    return select * from quiz where owner_id = @ownerId

GO

CREATE OR ALTER VIEW findQuizPublic AS
    SELECT * FROM quiz WHERE is_public=1

GO

CREATE OR ALTER FUNCTION findQuizPublicByOwnerId(
    @ownerId bigint
)
RETURNS TABLE AS
    RETURN SELECT * FROM quiz WHERE owner_id = @ownerId AND is_public = 1