import React from "react";
import {GrantCall} from "../types/GrantCall.type";
import {Container, Table} from "react-bootstrap";
import {EvaluationPanel} from "../types/EvaluationPanel.type";


const EvaluationPanelList = (props:{panelList:EvaluationPanel[], handleClick:(grantId: number,panelId: number) => void}) => {


    return (
        <div className="innerBox">
            <h4>My Assigned To Review Grants</h4>
            <Table>
                <thead>
                <tr>
                    <th style={{width: "50%"}}>Title</th>
                    <th style={{width: "15%"}}>Funding</th>
                    <th style={{width: "15%"}}>Open Date</th>
                    <th style={{width: "15%"}}>Close Date</th>
                    <th style={{width: "5%"}}>Number of Applications</th>
                </tr>
                </thead>
                <tbody>
                {props.panelList.map(panel => (
                    <tr key={panel.grants[0].grantCallId} onClick={() => props.handleClick(panel.grants[0].grantCallId,panel.evaluationPanelId)}>
                        <td>{panel.grants[0].title} </td>
                        <td> {panel.grants[0].funding}</td>
                        <td> {panel.grants[0].openDate}</td>
                        <td>{panel.grants[0].closeDate} </td>
                        <td>{panel.grants[0].numberApps} </td>
                    </tr>

                ))}

                </tbody>
            </Table>
        </div>

    )
}



export default EvaluationPanelList;