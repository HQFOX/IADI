import React, {useEffect, useState} from 'react';
import {useSelector} from "react-redux";
import {RootStore} from "../Store";
import ApplicationTable from "../components/ApplicationTable";
import SubmitAppModal from "../components/SubmitAppModal";
import AppDetails from "../components/AppDetails";

export interface Props {

}

function StudentApplications(props: Props) {
    const login = useSelector((state: RootStore) => state.login);

    useEffect(() => {
        fetchApps();
    }, []);

    const [tempApps, setTempApps] = useState([]);
    const [subApps, setSubApps] = useState([]);
    const [finalSubmit, setFinalSubmit] = useState(false);
    const [finalSubmitID, setFinalSubmitID] = useState(0);
    const [detailsSubmit, setDetailsSubmit] = useState(false);
    const [detailsSubmitID, setDetailsSubmitID] = useState(0);

    const finalSubmissionForm = (id: number) => {
        console.log("Finish submission for app " + id);
        setFinalSubmitID(id);
        setFinalSubmit(true);
    }

    const detailsForm = (id: number) => {
        console.log("Show Details for app " + id);
        setDetailsSubmitID(id);
        setDetailsSubmit(true);
    }

    const handleClose = () => {
        fetchApps();
        setFinalSubmit(false);
        setDetailsSubmit(false);
    }



    const fetchApps = async () => {
        fetch('/student/' + login.currentId + '/application')
            .then(res => res.json())
            .then(json => {
                setTempApps(json.filter((p: any) => p.status == 'Temporary'));
                setSubApps(json.filter((p: any) => p.status != 'Temporary'));
            });
    }

    return (
        <>
            {finalSubmit == true ? (<SubmitAppModal show={finalSubmit} handleClose={handleClose} app={tempApps.find((x:any) => x.applicationId === finalSubmitID)} />) : (null)}
            {detailsSubmit == true ? (<AppDetails show={detailsSubmit} handleClose={handleClose} app={subApps.find((x:any) => x.applicationId === detailsSubmitID)} />) : (null)}
            <div className="parent-div">
                <div className="child-div-left">
                    <ApplicationTable data={tempApps} tableTitle={"Temporary Applications"}
                                      subForm={finalSubmissionForm}/>
                </div>
                <div className="child-div-right">
                    <ApplicationTable data={subApps} tableTitle={"Final Applications"}
                                        subForm={detailsForm}/>
                </div>
            </div>
        </>
    )
}

export default StudentApplications;