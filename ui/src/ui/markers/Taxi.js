import React, { useState } from 'react';
import { faHome, faUserAstronaut, faTaxi } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Marker, Popup, Tooltip } from 'react-leaflet';
import ReactDOMServer from 'react-dom/server';
import * as L from 'leaflet';
import axios from 'axios';
import MarkerPopup from './MarkerPopup';

function Taxi(props) {
    const [notifications, setNotifications] = useState([]);
    
    const iconHTML = ReactDOMServer.renderToString(<FontAwesomeIcon icon={faTaxi}/>)
    const customMarkerIcon = L.divIcon({
      html: iconHTML,
      className: 'marker-taxi'
    });

    const eventHandlers={
      popupopen: (e) => {
        console.log(e);

        const url = `http://localhost:8080/api/notifications/${props.id}`;

        axios.get(url)
        .then(function (response) {
          console.log(response);
          setNotifications(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });

      },
      popupclose: (e) => {
        console.log("close")
      }
    };

    return (
        <Marker  icon={customMarkerIcon} position={props.position} eventHandlers={eventHandlers}>  
          <MarkerPopup id={props.id} position={props.position} notifications={notifications}></MarkerPopup>
        </Marker>
      );
  }
  
  export default Taxi;