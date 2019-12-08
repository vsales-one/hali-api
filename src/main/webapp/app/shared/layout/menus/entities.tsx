import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <MenuItem icon="asterisk" to="/entity/category">
      Category
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/city">
      City
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/district">
      District
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/posting-item">
      Posting Item
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/user-profile">
      User Profile
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/post-favorite">
      Post Favorite
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
