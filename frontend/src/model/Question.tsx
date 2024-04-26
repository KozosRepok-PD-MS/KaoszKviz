import LocalDateTime from "ts-time/LocalDateTime";

export interface Question {
    id?: bigint;
	quizId: string;//!TODO
    createdAt?: LocalDateTime;
	questionType?: string;
	question?: String;
	timeToAnswer?: number;
}

/* export interface Question {
    id?: bigint;
	quiz?: Map<String, String>;
    createdAt?: LocalDateTime;
	questionType?: string;
	question?: String;
	timeToAnswer?: number;
} */
export type TQuestionList = Question[];

export interface QuestionCreateFormType {
    quizId?: String;
	question?: String;
	questionType?: string;
	timeToAnswer?: number;
}

export interface QuestionModifyFormType {
	id?: String;
	question?: String;
	timeToAnswer?: number;
}
