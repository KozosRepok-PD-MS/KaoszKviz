
export interface Quiz {
    id?: number;
    isPublic?: boolean;
    ownerId?: number;
    title?: string;
    description?: string;
    shortAccessibleName?: string;
    mediaOwnerId?: string;
    mediaFileName?: string;
}

export type TQuizList = Quiz[];

export interface QuizCreateFormType {
    ownerId?: bigint;
    title: string;
    description: string;
    isPublic: boolean;
    shortAccessibleNAme: string;
}

export interface QuizModifyFormType {
    id?: String; //!TODO nem string
    title: string;
    description: string;
    isPublic: boolean;
    shortAccessibleNAme: string;
}
