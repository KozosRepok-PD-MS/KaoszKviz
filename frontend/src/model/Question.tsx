import LocalDateTime from "ts-time/LocalDateTime";

/* export interface Question {//!TODO Backend még rosszul adja a választ. Át kell majd írni
    id?: bigint;
	quizId: bigint;
    createdAt?: LocalDateTime;
	questionType?: string;
	question?: String;
	timeToAnswer?: number;
} */

export interface Question {
    id?: bigint;
	quiz?: Map<String, String>;
    createdAt?: LocalDateTime;
	questionType?: string;
	question?: String;
	timeToAnswer?: number;
}
export type TQuestionList = Question[];

export interface QuestionCreateFormType {
    quizId?: String;
	question?: String;
	questionType?: string;
	timeToAnswer?: number;
}

export interface QuizModifyFormType {
	quizId?: String;
	question?: String;
	timeToAnswer?: number;
}
