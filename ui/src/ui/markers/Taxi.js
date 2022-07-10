import React, { useState } from 'react';
import { faHome, faUserAstronaut, faTaxi } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Marker, Popup, Tooltip, Polyline } from 'react-leaflet';
import ReactDOMServer from 'react-dom/server';
import * as L from 'leaflet';
import axios from 'axios';
import MarkerPopup from './MarkerPopup';

function Taxi(props) {
    const [notifications, setNotifications] = useState([]);
    // [52.5200, 13.4050],
    //   [52.51, 13.5050]
    const [polylineToPassanger, setPolylineToPassanger] = useState([]);
    const [polylineToDestination, setPolylineToDestination] = useState([]);
      
    

    const iconHTML = ReactDOMServer.renderToString(<FontAwesomeIcon icon={faTaxi}/>)
    const customMarkerIcon = L.divIcon({
      html: iconHTML,
      className: 'marker-taxi'
    });

    const eventHandlers={
      popupopen: (e) => {
        props.onSelect && props.onSelect(props.id);
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


        const urlll = `http://localhost:8080/api/contract/${props.type}/${props.id}`;
        axios.get(urlll)
        .then(function (response) {
          const data = response.data;
          
          console.log(data.pathTaxiToPassanger);
          if (data.pathTaxiToPassanger) {
            setPolylineToPassanger(data.pathTaxiToPassanger);
          }
          if (data.pathTaxiToDestination) {
            setPolylineToDestination(data.pathTaxiToDestination);
          }

          
          
        })
        .catch(function (error) {
          console.log(error);
        });
        
      },
      popupclose: (e) => {
        console.log("close")

        setPolylineToDestination([]);
        setPolylineToPassanger([]);
      }
    };


    return (
      <>
        <Polyline color="green" positions={polylineToPassanger} />
        <Polyline color="red" positions={polylineToDestination} />
        <Marker icon={customMarkerIcon} position={props.position} eventHandlers={eventHandlers}>  
          <MarkerPopup id={props.id} position={props.position} notifications={notifications} status={props.status}></MarkerPopup>
        </Marker>
      </>
        
      );
  }
  
  export default Taxi;