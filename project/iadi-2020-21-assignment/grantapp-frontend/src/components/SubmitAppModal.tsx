import * as React from 'react';
import {ChangeEvent, useEffect, useState} from 'react';
import "../css/routerpage.css"
import {Accordion, Button, Card, Form, Modal} from 'react-bootstrap';
import {MdDescription, MdTitle} from "react-icons/md";
import {
    AiOutlineSchedule,
    BiCheckSquare,
    ImAttachment,
    IoIosGitNetwork,
    RiFilePaper2Line,
    VscDebugStart
} from "react-icons/all";
import {useSelector} from "react-redux";
import {RootStore} from "../Store";
import {fullRequest, semiRequest} from "../utils/StudentAppUtil";

export interface Props {
    show: boolean;
    handleClose: () => void;
    app: any
}

function SubmitAppModal({app, show, handleClose}: Props) {
    useEffect(() => {
        fetchDataItems();
    }, []);

    const login = useSelector((state: RootStore) => state.login);
    const handleIntro = (e: ChangeEvent<HTMLTextAreaElement>) => setIntro(e.target.value);
    const handlePub = (e: ChangeEvent<HTMLTextAreaElement>) => setPubs(e.target.value);
    const handleRelated = (e: ChangeEvent<HTMLTextAreaElement>) => setRelated(e.target.value);
    const handleWork = (e: ChangeEvent<HTMLTextAreaElement>) => setWork(e.target.value);
    const [intro, setIntro] = useState(app.introduction);
    const [work, setWork] = useState(app.workPlan);
    const [related, setRelated] = useState(app.relatedWork);
    const [pubs, setPubs] = useState(app.publications);
    const [dataitems, setDataitems] = useState([]);
    const [dataitemsstring, setDataitemsString] = useState("");

    //TODO
    const [file, setFile] = useState("");
    const [dataItemID, setdataItemID] = useState(0);
    const [reqFileSub, setReqFileSub] = useState(false);

    const fetchDataItems = async () => {
        fetch('/grantcall/' + app.grantId + "/dataitems")
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


    const submitForm = async (state: string) => {
        let request = (isCompleted() ? fullRequest(login.currentId, app.grantId, intro, related, work, pubs, state, dataItemID, file)
            : semiRequest(login.currentId, app.grantId, intro, related, work, pubs, state))
        fetch('/application/' + app.applicationId, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(request)
        })
            .then(response => {
                if (response.ok) {
                    handleClose();
                } else {
                    window.alert(`Error: ${response.status}: ${response.statusText}`)
                    return null;
                }
            })
            .catch(err => {
                console.log(err);
                return null
            })
    }

    const deleteApp = async () => {
        fetch('/application/' + app.applicationId, {method: "DELETE"})
            .then(response => {
                if (response.ok) {
                    handleClose();
                } else {
                    window.alert(`Error: ${response.status}: ${response.statusText}`)
                    return null;
                }
            })
            .catch(err => {
                console.log(err);
                return null
            })
    }

    const handleFileInput = (e:any, item:any, mandatory:boolean) => {
        console.log(item);
        setdataItemID(item);
        console.log(e.target.files[0].name);
        setFile(e.target.files[0].name);
        if(mandatory)
            setReqFileSub(true);
    }

    const isCompleted = () => (intro != "" && work != "" && related != "" && pubs != "");

    return (
        <Modal centered size="lg" show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>Grant Call Application</Modal.Title>
            </Modal.Header>
            <Form>
                <Modal.Body>
                    <div><MdTitle className="icon"/>{app.grantTitle}</div>
                    <div><MdDescription className="icon"/>Description: <br/>
                        <textarea readOnly defaultValue={app.grantDescription}/></div>
                    <hr/>
                    <table width={"100%"}>
                        <tr>
                            <td>
                                <VscDebugStart className="icon"/>Introduction:
                                <textarea onChange={handleIntro} defaultValue={intro}/>
                            </td>
                            <td>
                                <AiOutlineSchedule className="icon"/>Work Plan:
                                <textarea onChange={handleWork} defaultValue={work}/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <IoIosGitNetwork className="icon"/>Related Work:
                                <textarea onChange={handleRelated} defaultValue={related}/>
                            </td>
                            <td>
                                <RiFilePaper2Line className="icon"/>Publications:
                                <textarea onChange={handlePub} defaultValue={pubs}/>
                            </td>
                        </tr>
                    </table>
                    <hr/>
                    <Accordion>
                        {dataitems.map((item: any) => (
                            <Card>
                                <Card.Header>
                                    <Accordion.Toggle as={Button} variant="link" eventKey={item.dataItemId}>
                                        <ImAttachment/> {item.dataType} {item.mandatory == true ? (
                                        <div className="dataitem-mandatory">Mandatory Item <BiCheckSquare/>
                                        </div>) : (null)}
                                    </Accordion.Toggle>
                                </Card.Header>
                                <Accordion.Collapse eventKey={item.dataItemId}>
                                    <Card.Body>
                                        <div className="mb-3">
                                            <Form.File id="formcheck-api-regular">
                                                <Form.File.Label>Upload file</Form.File.Label>
                                                <Form.File.Input onChange={(e:any) => handleFileInput(e,item.dataItemId,item.mandatory)} />
                                            </Form.File>
                                        </div>
                                    </Card.Body>
                                </Accordion.Collapse>
                            </Card>
                        ))}
                    </Accordion>
                </Modal.Body>
                <Modal.Footer>
                    <Button className="leftButton" variant="danger" onClick={deleteApp}>
                        Delete Application
                    </Button>
                    <Button variant="outline-success" onClick={() => submitForm("Temporary")}>
                        Save as Draft
                    </Button>
                    {isCompleted() == true ? (<Button variant="success" onClick={() => submitForm("Submitted")}>Submit
                            Application</Button>)
                        : (<Button variant="success" disabled={true}>Submit Application</Button>)}
                </Modal.Footer>
            </Form>
        </Modal>
    )
}

export default SubmitAppModal;