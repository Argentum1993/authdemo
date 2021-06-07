import React, {useRef, useState, useEffect} from "react";
import ReactDOM from 'react-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faUnlock } from '@fortawesome/free-solid-svg-icons'
import { faTrash } from '@fortawesome/free-solid-svg-icons'

import UserService from '../services/user.service'
import AuthService from "../services/auth.service";


const Users = (props) => {
    const [rows, setRows] = useState("");
    const [mainCheckBox, setMainCheckBox] = useState('');
    let refs = useRef(new Array());
    let refsId = useRef(new Array());

    useEffect(() => {
        updateUsers();
    }, []);

    const updateUsers = () => {
        UserService.getAllUsers().then(
            (response) => {
                const users = response.data;
                const rows = [];

                //getCheckedIds(true);
                //setMainCheckBox('');
                refs.current = [];
                refsId.current = [];
                users.map((user) => {
                    rows.push(
                        <tr>
                            <td scope="col">
                                <input type="checkbox" ref={(e) => refs.current.push(e)} onChange={pushItemCheckBox}/>
                            </td>
                            <td scope="col" ref={e => refsId.current.push(e)}>{user.id}</td>
                            <td scope="col">{user.name}</td>
                            <td scope="col">{user.email}</td>
                            <td scope="col">{user.regDate}</td>
                            <td scope="col">{user.lastLogin}</td>
                            <td scope="col">{user.banned ? "banned" : "active"}</td>
                        </tr>
                    )
                });
                setRows(rows);
            },
            (error) => {
                const resMessage =
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString();
                if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                    AuthService.logout()
                } else {
                    alert(resMessage)
                }
            })
    }

    const pushMainCheckBox = ({ target: { checked } }) => {
        if (!mainCheckBox || mainCheckBox !== 'checked') {
            if (refs.current.length > 0){
                refs.current.map((e) =>{
                    if (ReactDOM.findDOMNode(e) !== null)
                        ReactDOM.findDOMNode(e).checked = true;
                    }
                );
            }
            setMainCheckBox('checked');
        } else {
            if (refs.current.length > 0){
                refs.current.map((e) =>{
                        if (ReactDOM.findDOMNode(e) !== null)
                            ReactDOM.findDOMNode(e).checked = false;
                    }
                )
            }
            setMainCheckBox('')
        }
    }

    const pushItemCheckBox = ({ target: { checked } }) => {

        if (checked) {
            setMainCheckBox('checked');
        } else {
            let isFree = true;
            for (let i = 0; i < refs.current.length; i++) {
                if (ReactDOM.findDOMNode(refs.current[i]) && ReactDOM.findDOMNode(refs.current[i]).checked) {
                    isFree = false;
                    break;
                }
            }
            if (isFree) {
                setMainCheckBox('')
            }
        }
    }

    const getCheckedIds = (setUncheck = false) => {
        let items = [];

        for (let i = 0; i < refs.current.length; i++) {
            if (ReactDOM.findDOMNode(refs.current[i]) && ReactDOM.findDOMNode(refs.current[i]).checked){
                if (setUncheck){
                    ReactDOM.findDOMNode(refs.current[i]).checked = false;
                }
                items.push(ReactDOM.findDOMNode(refsId.current[i]).innerHTML);
            }
        }
        if (setUncheck){
            setMainCheckBox('');
        }
        return items;
    }

    const  deleteUsers = () => {
        let ids = getCheckedIds(true);
        UserService.deleteUsers(ids).then(
            (response) => {
                setMainCheckBox('');
                updateUsers();
                }, (error) => {
                const resMessage =
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString();
                if (error.response.status === 401 || error.response.status === 403) {
                    AuthService.logout()
                } else {
                    alert(resMessage)
                }
            })
    }

    const  block = () => {
        let ids = getCheckedIds();
        UserService.blockUsers(ids).then(
            (response) => {
                updateUsers();
            }, (error) => {
                const resMessage =
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString();
                if (error.response.status === 401 || error.response.status === 403) {
                    AuthService.logout()
                } else {
                    alert(resMessage)
                }
            })
    }

    const  unBlock = () => {
        let ids = getCheckedIds();
        UserService.unBlockUsers(ids).then(
            (response) => {
                updateUsers();
            }, (error) => {
                const resMessage =
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString();
                if (error.response.status && (error.response.status === 401 || error.response.status === 403)) {
                    AuthService.logout()
                } else {
                    alert(resMessage)
                }
            })
    }

    return (
        <div className="container">
            <div className="btn-group mb-1" role="group" aria-label="Basic outlined example">
                <button type="button" className="btn btn-outline-secondary" onClick={block}>Block</button>
                <button type="button" className="btn btn-outline-secondary" onClick={unBlock}>
                    <FontAwesomeIcon icon={faUnlock} />
                </button>
                <button type="button" className="btn btn-outline-secondary" onClick={deleteUsers}>
                    <FontAwesomeIcon icon={faTrash} />
                </button>
            </div>
            <div className="row" >
                <div className="col s12 board">
                    <table class="table table-striped table-dark">
                        <thead>
                        <tr>
                            <td scope="col">
                                <input type="checkbox" onClick={pushMainCheckBox} checked={mainCheckBox} />
                            </td>
                            <td scope="col">id</td>
                            <td scope="col">name</td>
                            <td scope="col">email</td>
                            <td scope="col">registration</td>
                            <td scope="col">last login</td>
                            <td scope="col">status</td>
                        </tr>
                        </thead>
                        <tbody>
                        {rows}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default Users;