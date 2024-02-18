use kaoszkviz

create function findMediaByOwnerIdAndFileName
(
	@ownerId bigint,
	@fileName nvarchar(30)
)
returns table as
	return select * from media where owner_id = @ownerId and file_name like @fileName
