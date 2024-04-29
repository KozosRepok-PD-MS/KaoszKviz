
create or alter function findAnswerByQuestionId
(
	@questionId bigint
)
returns table as
	return select * from answer where question_id = @questionId

GO
