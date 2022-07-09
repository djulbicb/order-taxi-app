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
            <br></br>
            <p>{props.status}</p>
            <p>
              {JSON.stringify(props)}
            </p>
            {lis}
        </Popup>
      );
  }
  
  export default MarkerPopup;