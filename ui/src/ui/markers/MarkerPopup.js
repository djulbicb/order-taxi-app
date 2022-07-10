import React, { useEffect, useState } from 'react';
import { Popup } from 'react-leaflet';
import axios from 'axios';

function MarkerPopup(props) {

   

    useEffect(()=> {
       
  }, [])
  const lis = props.notifications?.map(elem => {
    return (<li>{elem.message}</li>)
  })

    return (
        <Popup>
            <h3>{props.id}</h3>
            <p>{props.status}</p>
            <hr></hr>
            {lis}
        </Popup>
      );
  }
  
  export default MarkerPopup;