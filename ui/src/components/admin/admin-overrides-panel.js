import * as React from "react";
import Paper from "@mui/material/Paper";
import {
  Button,
  Card,
  CardContent,
  Checkbox,
  FormControl,
  FormControlLabel,
  FormLabel,
  Grid,
  Radio,
  RadioGroup,
  Slider,
  TextField,
  Typography,
} from "@mui/material";
import axios from "axios";

export default function AdminOverridesPanel(props) {

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

  const handleAddTaxiToCentralZone = () => {{
    const url = `http://localhost:8080/api/central/taxi/${taxiToCentralZone}`;
    axios.get(url)
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
  }}

  const handleAddPassangerToCentralZone = () => {{
    const url = `http://localhost:8080/api/central/passangers/${passangerToCentralZone}`;
    axios.get(url)
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
  }}

  const handleAddTaxiToEveryZone = () => {{
    const url = `http://localhost:8080/api/every-zone/taxi/${taxiToEveryZone}`;
    axios.get(url)
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
  }}

  const handleAddPassangerToEveryZone = () => {{
    const url = `http://localhost:8080/api/every-zone/passangers/${passangerToEveryZone}`;
    axios.get(url)
    .then(function (response) {
      console.log(response);
    })
    .catch(function (error) {
      console.log(error);
    });
  }}

    return (
      <>
        <Typography>Override Control:</Typography>
        <Button fullWidth variant="outlined" onClick={handleDeleteAll}>
          Delete all
        </Button>

        <hr />

        <Grid container spacing={1}>
          <Grid item xs={12}>
            <Typography>Add to central zone:</Typography>
          </Grid>
          <Grid item xs={10}>
            <TextField
              type="number"
              size="small"
              label="Taxi"
              value={taxiToCentralZone}
              onChange={(e)=>{setTaxiToCentralZone(e.target.value)}}
            />
          </Grid>
          <Grid item xs={2}>
            <Button variant="outlined" onClick={handleAddTaxiToCentralZone}>+</Button>
          </Grid>

          <Grid item xs={10}>
            <TextField
              type="number"
              size="small"
              label="Passanger"
              value={passangerToCentralZone}
              onChange={(e)=>{setPassangerToCentralZone(e.target.value)}}
            />
          </Grid>
          <Grid item xs={2}>
            <Button variant="outlined" onClick={handleAddPassangerToCentralZone}>+</Button>
          </Grid>
        </Grid>

        <hr />

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
              onChange={(e)=>{setTaxiToEveryZone(e.target.value)}}
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
              onChange={(e)=>{setPassangerToEveryZone(e.target.value)}}
            />
          </Grid>
          <Grid item xs={2}>
            <Button variant="outlined" onClick={handleAddPassangerToEveryZone}>+</Button>
          </Grid>
          <Grid>
            {taxiToCentralZone} {taxiToEveryZone}
            {passangerToCentralZone} {passangerToEveryZone}
          </Grid>
        </Grid>
      </>
    );
  }

