import React from 'react';
import Card from '../Cards/Cards';
import { data } from '../../common/lib';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';

const Panel = () => {
  return (
    <>
      <AppBar position="static">
        <Toolbar>
          <IconButton edge="start"  color="inherit" aria-label="menu"/>
          <Typography variant="h6">
            Public enemy
          </Typography>
        </Toolbar>
      </AppBar>
      {data.map(item => (
        <Card element = {item} key={item.title} />
      ))}
    </>
  );
};
export default Panel;
