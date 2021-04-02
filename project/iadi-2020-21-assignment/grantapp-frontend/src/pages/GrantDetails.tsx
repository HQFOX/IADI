import React, {useEffect, useState} from 'react';
import {RouteComponentProps} from "react-router-dom";
import '../css/routerpage.css'
import {Spinner} from "react-bootstrap";
import GrantDetailsComponent from "../components/GrantDetails";
import ApplicationsTable from "../components/ApplicationsTable";

interface Grant {
    grantCallId: number,
    title: string,
    description: string,
    requirements: string,
    funding: number,
    openDate: Date,
    closeDate: Date,
    sponsorId: number
}

interface DataItem {
    dataItemId: number,
    dataType: string,
    mandatory: boolean
}

type TParams = { id: string };

function GrantDetails({match}: RouteComponentProps<TParams>) {

    useEffect(() => {
        fetchGrant();
    }, []);

    const [grantDetails, setGrantDetails] = useState({} as Grant);
    const [apps, setApps] = useState([]);
    const [dataitems, setDataitems] = useState([]);
    const [dataitemsstring, setDataitemsString] = useState("");
    const [isLoaded, setIsLoaded] = useState(false);

    const refreshTable = () => fetchApps();

    const fetchGrant = async () => {
        fetch('/grantcall/' + match.params.id)
            .then(res => res.json())
            .then(data => {
                setGrantDetails(data);
                setIsLoaded(true);
                fetchApps();
                fetchDataItems();
            })
    }

    const fetchApps = async () => {
        fetch('/grantcall/' + match.params.id + "/applications")
            .then(res => res.json())
            .then(data => {
                setApps(data.filter((a:any) => a.status != 'Temporary'));
            })
    }

    const fetchDataItems = async () => {
        fetch('/grantcall/' + match.params.id + "/dataitems")
            .then(res => res.json())
            .then(data => {
                setDataitems(data);
                var items = "";
                data.map((item: any) => {
                    items = items + "[Mandatory:" + item.mandatory.toString() + "] " + item.dataType + "\n";
                })
                setDataitemsString(items);
            })
    }

    if (!isLoaded)
        return (
            <div className="loading"><Spinner animation="border"/></div>
        )
    else
        return (
            <>
                <div className="innerBox">
                    <GrantDetailsComponent dataitems={dataitemsstring} grantDetails={grantDetails}/>
                    <ApplicationsTable apps={apps} grantTitle={grantDetails.title} grantDesc={grantDetails.description} dataitems={dataitems} grantId={grantDetails.grantCallId} refreshTable={refreshTable}/>
                </div>
            </>
        )
}

export default GrantDetails;