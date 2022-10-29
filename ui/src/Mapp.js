
import React, { useEffect, useState } from "react";
import { MapContainer, Polyline, TileLayer } from "react-leaflet";
import GridLayer from "./ui/grid/GridLayer";
import Person from "./ui/markers/Person";
import Taxi from "./ui/markers/Taxi";
import RoutineMachine from "./RoutineMachine";

import Paper from '@mui/material/Paper';
import { Button, ButtonGroup, Card, CardContent, Checkbox, FormControl, FormControlLabel, FormLabel, Grid, Radio, RadioGroup, Slider, TextField, Typography } from "@mui/material";
import { styled } from '@mui/material/styles';
import PlaceholderLayer from "./ui/grid/PlaceholderLayer";
import MinimumDistanceSlider from "./components/minimum_distance_slider/MinimumDistanceSlider";
import AdminOverridesPanel from "./components/admin/admin-overrides-panel";
import AdminStatistics from "./components/admin/admin-statistics";
import LayerRouteAllObjects from "./ui/routes/LayerRouteAllObjects";
import LayerRouteSelectedObject from "./ui/routes/LayerRouteSelectedObject";
import axios from "axios";

const fillBlueOptions = { fillColor: 'blue' }

const Mapp = (props) => {
  const [mapTileType, setMapTileType] = useState("ArcGIS");
  const [timeoutInterval, setTimeoutInterval] = useState(3000);

  const [showRouting, setShowRouting] = useState(false);
  const [showGrid, setShowGrid] = useState(false);
  const [showPlaceholder, setShowPlaceholder] = useState(false);
  
  // Routes - all
  const [showRoutesAll, setShowRoutesAll] = useState(false);
  const handleShowRoutesAll = () => {
    setShowRoutesAll(!showRoutesAll)
  }
  const [showRoutesAllForceRerender, setShowRoutesAllForceRerender] = useState(0);
  const handleForceAllRoutesRerender = () => {
    setShowRoutesAllForceRerender(showRoutesAllForceRerender + 1)
  }

  // Routes - selected object
  const [showSelectedObject, setShowSelectedObject] = useState(true);
  const [selectedObject, setSelectedObject] = useState({});
  const onSelectObjectChange = (objectId, objectType) => {
    setSelectedObject({
      id: objectId,
      type: objectType
    });
  }
  const handleShowSelectObject = () => {
    setShowSelectedObject(!showSelectedObject)
  }

  const [placeholderType, setPlaceholderType] = useState('nearest');


  const [center, setCenter] = useState([52.5200, 13.4050])
  const [viewportObjectsIds, setViewportObjectsIds] = useState([]);
  const [viewportObjects, setViewportObjects] = useState([]);

  


  /*  VIEWPORT OBJECT IDS*/
  useEffect( () => {
    let timeoutId;
    const api = async () => {
       console.log('hello' + timeoutInterval)
       timeoutId = setTimeout(api, timeoutInterval);

       console.log("Timer")
       const request = {
         lat: center[0],
         lng: center[1],
         objects: ["PERSON", "PASSANGER"],
         size: "SIZE_3",
         layer: "ALL"
       }
 
       const params = getParam(request)
 
       fetch(`http://localhost:8080/viewport/objects-in-area?${params}`)
         .then(response => response.json())
         .then(data => {
           console.log("sss");
           setViewportObjects(data)
         }) 


    };
    api();
    return () => {
     clearTimeout(timeoutId);
    };
   }, [timeoutInterval]);

  // useEffect(() => {
  //   const timer = setTimeout(() => {
  //     console.log("Timer")
  //     const request = {
  //       lat: center[0],
  //       lng: center[1],
  //       objects: ["PERSON", "PASSANGER"],
  //       size: "SIZE_3",
  //       layer: "ALL"
  //     }

  //     const params = getParam(request)

  //     fetch(`http://localhost:8080/viewport/objects-in-area?${params}`)
  //       .then(response => response.json())
  //       .then(data => {
  //         console.log("sss");
  //         setViewportObjects(data)
  //       })
  //   }, 1000);
  //   return () => clearTimeout(timer);
  // }, [center]);

  // useEffect(() => {
  //   const intervalId = setInterval(() => {

  //     const request = {
  //       lat: center[0],
  //       lng: center[1],
  //       objects: ["PERSON", "PASSANGER"],
  //       size: "SIZE_3",
  //       layer: "ALL"
  //     }

  //     const params = getParam(request)

  //     fetch(`http://localhost:8080/viewport/objects-in-area?${params}`)
  //       .then(response => response.json())
  //       .then(data => {
  //         setViewportObjects(data)
  //       })
  //   }, 1000);

  //   return () => clearInterval(intervalId); //This is important
  // }, [center])



  const [marker, setMarker] = useState([52.5200, 13.4050]);


  const getParam = (o, searchParam = new URLSearchParams) => {
    Object.entries(o).forEach(([k, v]) => {
      if (v !== null && typeof v === 'object')
        getParam(v, searchParam)
      else
        searchParam.append(k, v)
    })
    return searchParam
  }


  const handleMapClick = () => {
    console.log("sss")
    setSelectedObject({})
  }
  const handleOnMoveEnd = (coordinate) => {
    setCenter([coordinate.lat, coordinate.lng]);

    // updateViewer(coordinate.lng, coordinate.lat)
  }



  const elements = viewportObjects.map((element, index) => {

    const lat = element.coordinate.lat;
    const lng = element.coordinate.lng;
    const coordinate = [lat, lng];


    if (element.type === "TAXI") {
      return <Taxi key={index} id={element.id} position={coordinate} status={element.status} onSelect={onSelectObjectChange} type="TAXI"></Taxi>
    } else if (element.type === "PASSANGER") {
      return <Person key={index} id={element.id} position={coordinate} status={element.status} onSelect={onSelectObjectChange} type="PASSANGER"></Person>
    }
  })



  const handleShowRouting = (event) => {
    setShowRouting(!showRouting)
  }
  const handleShowGridChange = (event) => {
    setShowGrid(!showGrid);
  };
  const handleShowPlaceholders = (event) => {
    setShowPlaceholder(!showPlaceholder)
  };
  const handelPlaceholderTypeChange = (event) => {
    setPlaceholderType(event.target.value)
  };
  const handelViewFilter = (event) => {
    console.log(event);
  };


  const handleMapTileChange = (event) => {
    const mapTileType = event.target.value;
    console.log(mapTileType)
    if (mapTileType === "docker") {
      setMapTileType("docker")
    } else if (mapTileType === "ArcGIS") {
      setMapTileType("ArcGIS")
    } else if (mapTileType === "java" ) {
      setMapTileType("java")
    } else {
      console.log("Unknown map tile type")
    }
  }

/* ADMIN STATISTICS PANEL */
const [taxiToEveryZone, setTaxiToEveryZone] = React.useState(2);
const [passangerToEveryZone, setPassangerToEveryZone] = React.useState(3);
const [taxiToCentralZone, setTaxiToCentralZone] = React.useState(2);
const [passangerToCentralZone, setPassangerToCentralZone] = React.useState(3);

const handleDeleteAll = () => {
  const url = `http://localhost:8080/api/overrides/delete-all`;
  axios.get(url)
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
}

const handleAddTaxiToCentralZone = () => {
  {
    const url = `http://localhost:8080/api/central/taxi/${taxiToCentralZone}`;
    axios.get(url)
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
}

const handleAddPassangerToCentralZone = () => {
  {
    const url = `http://localhost:8080/api/central/passangers/${passangerToCentralZone}`;
    axios.get(url)
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
}

const handleAddTaxiToEveryZone = () => {
  {
    const url = `http://localhost:8080/api/every-zone/taxi/${taxiToEveryZone}`;
    axios.get(url)
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
}

const handleAddPassangerToEveryZone = () => {
  {
    const url = `http://localhost:8080/api/every-zone/passangers/${passangerToEveryZone}`;
    axios.get(url)
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
}

const handleMoveIncrementChange = (moveIncrement) => {
  {
    const url = `http://localhost:8080/api/overrides/move-increment/${moveIncrement}`;
    axios.get(url)
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
}

const handleUpdateSpeedChange = (updateSpeed) => {
  {
    console.log("update speed " + updateSpeed)
    setTimeoutInterval(updateSpeed);
    const url = `http://localhost:8080/api/overrides/update-speed/${updateSpeed}`;
    axios.get(url)
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
}






  return (
    <>


      <Card className="admin-panel" variant="outlined">
        <CardContent>

          <Grid container>

            <Grid item xs={12}>
              <FormLabel id="demo-radio-buttons-group-label">Map</FormLabel>
            </Grid>
            <RadioGroup className="flex-row" defaultValue="ArcGIS" name="radio-buttons-group" onChange={handleMapTileChange}>
              <Grid item xs={4}>
                <FormControlLabel value="docker" control={<Radio />} label="Docker" />
              </Grid>
              <Grid item xs={4}>
                <FormControlLabel selected value="ArcGIS" control={<Radio />} label="ArcGIS" />
              </Grid>
              <Grid item xs={4}>
                <FormControlLabel value="java" control={<Radio />} label="Java" />
              </Grid>
            </RadioGroup>


            <Grid item xs={12}>
              <FormLabel id="demo-radio-buttons-group-label">Grid</FormLabel>
            </Grid>
            <Grid item xs={6}>
              <FormControlLabel control={<Checkbox checked={showGrid} onChange={handleShowGridChange} />} label="Show grid" />
            </Grid>
            <Grid item xs={6}>
              <FormControlLabel control={<Checkbox checked={showRouting} onChange={handleShowRouting} />} label="Show routing" />
            </Grid>


            {/* Routes */}
            <Grid item xs={12}>
              <FormLabel id="demo-radio-buttons-group-label">All routes</FormLabel>
            </Grid>
            <Grid item xs={6}>
              <FormControlLabel control={<Checkbox checked={showRoutesAll} onChange={handleShowRoutesAll} />} label="Show routes" />
            </Grid>
            <Grid item xs={6}>
              <Button fullWidth variant="outlined" onClick={handleForceAllRoutesRerender}>Reload</Button>
            </Grid>

            {/* SelectedObject */}
            <Grid item xs={12}>
              <FormLabel id="demo-radio-buttons-group-label">Selected object</FormLabel>
            </Grid>
            <Grid item xs={6}>
              <FormControlLabel control={<Checkbox checked={showSelectedObject} onChange={handleShowSelectObject} />} label="Show" />
            </Grid>
            <Grid item xs={6}>
              <Button fullWidth variant="outlined" onClick={handleForceAllRoutesRerender}>Reload</Button>
            </Grid>


            <Grid item xs={12}>
              <FormLabel id="demo-radio-buttons-group-label">Placeholder</FormLabel>
            </Grid>
            <Grid item xs={12}>
              <FormControlLabel control={<Checkbox checked={showPlaceholder} onChange={handleShowPlaceholders} />} label="Show placeholders" />
            </Grid>
            <Grid item xs={12}>
              <RadioGroup className="flex-row" defaultValue="nearest" name="radio-buttons-group" onChange={handelPlaceholderTypeChange}>
                <Grid item xs={6}>
                  <FormControlLabel value="matrix" control={<Radio />} label="Matrix" />
                </Grid>
                <Grid item xs={6}>
                  <FormControlLabel value="nearest" control={<Radio />} label="Nearest" />
                </Grid>
              </RadioGroup>
            </Grid>

            <Grid item xs={12}>
            <Grid container spacing={1}>
        <Grid item xs={12}>
          <FormLabel id="demo-radio-buttons-group-label">Move increment</FormLabel>
          <ButtonGroup fullWidth variant="outlined" aria-label="outlined button group">
            <Button onClick={e=>handleMoveIncrementChange(100)}>100</Button>
            <Button onClick={e=>handleMoveIncrementChange(200)} color="success">200</Button>
            <Button onClick={e=>handleMoveIncrementChange(400)}>400</Button>
          </ButtonGroup>
        </Grid>
        <Grid item xs={12}>
          <FormLabel id="demo-radio-buttons-group-label">Update increment</FormLabel>
          <ButtonGroup fullWidth variant="outlined" aria-label="outlined button group">
            <Button onClick={e=>handleUpdateSpeedChange(250)}>250</Button>
            <Button onClick={e=>handleUpdateSpeedChange(500)}>500</Button>
            <Button onClick={e=>handleUpdateSpeedChange(1000)}color="success">1000</Button>
          </ButtonGroup>
        </Grid>
        
        <Grid item xs={12}>
          <Typography>Override Control:</Typography>
        </Grid>

        <Grid item xs={12}>
          <Button fullWidth variant="outlined" onClick={handleDeleteAll}>
            Delete all
          </Button>
        </Grid>

        <Grid item xs={12}>
          <Typography>Add to central zone:</Typography>
        </Grid>
        <Grid item xs={9}>
          <TextField
            type="number"
            size="small"
            label="Taxi"
            value={taxiToCentralZone}
            onChange={(e) => { setTaxiToCentralZone(e.target.value) }}
          />
        </Grid>
        <Grid item xs={1}>
          <Button variant="outlined" onClick={handleAddTaxiToCentralZone}>+</Button>
        </Grid>

        <Grid item xs={9}>
          <TextField
            type="number"
            size="small"
            label="Passanger"
            value={passangerToCentralZone}
            onChange={(e) => { setPassangerToCentralZone(e.target.value) }}
          />
        </Grid>
        <Grid item xs={1}>
          <Button variant="outlined" onClick={handleAddPassangerToCentralZone}>+</Button>
        </Grid>
      </Grid>

      <Grid container spacing={1}>
        <Grid item xs={12}>
          <Typography>Add to every zone:</Typography>
        </Grid>
        <Grid item xs={10}>
          <TextField
            type="number"
            size="small"
            label="Taxi"
            value={taxiToEveryZone}
            onChange={(e) => { setTaxiToEveryZone(e.target.value) }}
          />
        </Grid>
        <Grid item xs={2}>
          <Button variant="outlined" onClick={handleAddTaxiToEveryZone}>+</Button>
        </Grid>

        <Grid item xs={10}>
          <TextField
            type="number"
            size="small"
            label="Passanger"
            value={passangerToEveryZone}
            onChange={(e) => { setPassangerToEveryZone(e.target.value) }}
          />
        </Grid>
        <Grid item xs={2}>
          <Button variant="outlined" onClick={handleAddPassangerToEveryZone}>+</Button>
        </Grid>
      </Grid>
            </Grid>

          </Grid>
        </CardContent>
      </Card>

      <AdminStatistics />

      <MapContainer
        doubleClickZoom={false}
        preferCanvas={true}
        id="mapId"
        zoom={14}
        center={[52.5200, 13.4050]}
      >

        { mapTileType === "docker" && <TileLayer
          url="http://localhost:9001/tile/{z}/{x}/{y}.png"
          attribution="Tiles &copy; Esri &mdash; Sources: GEBCO, NOAA, CHS, OSU, UNH, CSUMB, National Geographic, DeLorme, NAVTEQ, and Esri"
        /> }
        { mapTileType === "ArcGIS" && <TileLayer
          url = "https://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile/{z}/{y}/{x}"
          attribution="Tiles &copy; Esri &mdash; Sources: GEBCO, NOAA, CHS, OSU, UNH, CSUMB, National Geographic, DeLorme, NAVTEQ, and Esri"
        /> }
        { mapTileType === "java" && <TileLayer
          url="http://localhost:8080/tile-test/v1/car/tile({x},{y},{z})"
          attribution="Tiles &copy; Esri &mdash; Sources: GEBCO, NOAA, CHS, OSU, UNH, CSUMB, National Geographic, DeLorme, NAVTEQ, and Esri"
        /> }

        {showPlaceholder && (<PlaceholderLayer placeholderType={placeholderType}></PlaceholderLayer>)}
        {showGrid && (<GridLayer onClick={handleMapClick} onMoveEnd={handleOnMoveEnd}></GridLayer>)}

        {/* Routes */}
        {showRoutesAll && (<LayerRouteAllObjects forceRerender={showRoutesAllForceRerender}/>)} 
        {/* Selected Object */}
        {showSelectedObject && selectedObject.id && (<LayerRouteSelectedObject selectedObject={selectedObject}/>)} 
        

        {elements}

        {showRouting && <RoutineMachine />}
      </MapContainer>


    </>
  );
};

export default Mapp;
