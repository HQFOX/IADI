import {EvaluationPanel} from "./EvaluationPanel.type";

export type Reviewer = {
    reviewerId:number,
    name:string
}

export type ReviewerPanels = {
    reviewerId:number,
    name:string
    evaluationPanels: EvaluationPanel[]
}
