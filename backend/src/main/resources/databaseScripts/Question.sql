use kaoszkviz

GO

create or alter function findQuestionByQuizId
(
	@quizId bigint
)
returns table as
	return select * from question where quiz_id = @quizId