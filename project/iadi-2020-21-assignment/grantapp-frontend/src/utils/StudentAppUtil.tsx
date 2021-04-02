export const semiRequest = (currentId: number, grantId: number, intro:String, related:String, work:String, pubs:String, state:String) => {
    return(
        {
            studentId: currentId,
            grantId: grantId,
            application: {
                introduction: intro,
                relatedWork: related,
                workPlan: work,
                publications: pubs,
                status: state
            }, dataItems: []})
}

export const fullRequest = (currentId: number, grantId: number, intro:String, related:String, work:String, pubs:String, state:String, dataItemID: number, file:String) => {
    return(
        {
            studentId: currentId,
            grantId: grantId,
            application: {
                introduction: intro,
                relatedWork: related,
                workPlan: work,
                publications: pubs,
                status: state
            }, dataItems: [
                {
                    appDataItemId: 1,
                    dataItemId: dataItemID,
                    data: file
                }
            ]})
}
