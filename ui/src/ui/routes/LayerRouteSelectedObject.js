
import React, { useEffect, useState } from "react";
import { Marker, Polyline, Popup, Tooltip, useMapEvents } from "react-leaflet";
import MapLabel from "../markers/MapLabel";
import axios from 'axios';
import { Button, IconButton } from "@mui/material";
import { LocalTaxi, PersonAddAlt1Outlined, Person } from '@mui/icons-material';
import styles from './LayerRouteSelectedObject.module.css';

const LayerRouteSelectedObject = (props) => {
    const [notifications, setNotifications] = useState([]);
    const [route, setRoute] = useState([]);

    useEffect(() => {
        console.log(props.selectedObject)
            
            const selected = props.selectedObject;

            if (selected?.id) {
                const url = `http://localhost:8080/api/notifications/${selected.id}`;

                axios.get(url)
                    .then(function (response) {
                    console.log(response);
                    setNotifications(response.data);
                    })
                    .catch(function (error) {
                    console.log(error);
                    });
            }


            if (selected?.id) {
                let url = `http://localhost:8080/api/ui/routes/${selected.type}/${selected.id}`
            axios.get(url).then((response) => {
                console.log(response.data)
                setRoute(response.data);
            })
            .catch((error) => {
                console.log(error);
            });

            } else {
                setRoute([]);
            }
            
    //         return () => clearInterval(intervalId); //This is important
        }, [props.selectedObject])


        const lis = notifications?.map((elem, index) => {
            return (<li key={"notify" + index}>{elem.message}</li>)
        })

        let allPolylines = [];
        if (route) {
            const path1 = (<Polyline key={`paht1${props.selectedObject.id}`} color="green" positions={route.pathTaxiToPassanger} />);
            const path2 = (<Polyline key={`paht2${props.selectedObject.id}`} color="red" positions={route.pathTaxiToDestination} />);
            route.pathTaxiToPassanger && allPolylines.push(path1);
            route.pathTaxiToDestination && allPolylines.push(path2);
        }

        return <>
            {allPolylines}
            
            ({notifications.length > 0 && (
                <div className={styles.notifications}>
                    <h3>Notifications:</h3>
                    <ul className={styles.list}>
                        {lis}
                    </ul>
                </div>)
            }
        </>
    }

export default LayerRouteSelectedObject;
