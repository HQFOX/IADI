import React from "react";

import {Container, Table} from "react-bootstrap";
import {Application} from "../types/Application.type";


function ApplicationList(props:{applicationList:Application[], handleClick:(id: number) => void}) {


    return (
        <div className="innerBox">
            <Table>
                <thead>
                <tr>
                    <th style={{width: "5%"}}>Application Number</th>
                    <th style={{width: "30%"}}>Introduction</th>
                    <th style={{width: "30%"}}>Related Work</th>
                    <th style={{width: "25%"}}>Publications</th>
                    <th style={{width: "10%"}}>Funded</th>
                </tr>
                </thead>
                <tbody>
                {props.applicationList.map( application => (
                    <tr key={application.applicationId} onClick={() => props.handleClick(application.applicationId)}>
                        <td>{application.introduction} </td>
                        <td>{application.relatedWork} </td>
                        <td>{application.publications} </td>
                        <td> </td>
                        <td> </td>
                    </tr>

                ))}

                </tbody>
            </Table>
        </div>

    )
}



export default ApplicationList;