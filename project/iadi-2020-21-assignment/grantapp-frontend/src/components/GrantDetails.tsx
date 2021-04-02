import {MdAttachMoney, MdDateRange, MdDescription, MdTitle} from "react-icons/md";
import {GiFeatheredWing} from "react-icons/gi";
import {AiOutlineEdit, AiOutlineSubnode} from "react-icons/ai";
import React from "react";

export interface Props {
    grantDetails: any,
    dataitems: string,
}

function GrantDetails({grantDetails, dataitems}: Props) {
    return (
        <div className="detail-container">
            <table width="100%">
                <tbody>
                <tr>
                    <td style={{width: "28%"}}><MdTitle className="icon"/>Title: {grantDetails.title}</td>
                    <td style={{width: "28%"}}><MdDateRange
                        className="icon"/>Date: {grantDetails.openDate} to {grantDetails.closeDate}</td>
                    <td style={{width: "28%"}}><GiFeatheredWing
                        className="icon"/>Sponsor: {grantDetails.sponsorId}
                    </td>
                    <td style={{width: "16%"}}><MdAttachMoney className="icon"/>Funding: {grantDetails.funding}
                    </td>
                </tr>
                <tr>
                    <td><MdDescription className="icon"/>Description: <br/>
                        <textarea readOnly defaultValue={grantDetails.description}/>
                    </td>
                    <td><AiOutlineEdit className="icon"/>Requirements: <br/>
                        <textarea readOnly defaultValue={grantDetails.requirements}/>
                    </td>
                    <td>
                        <AiOutlineSubnode className="icon"/>Submission Requirements: <br/>
                        <textarea readOnly defaultValue={dataitems}/>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    )
}

export default GrantDetails

