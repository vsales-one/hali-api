import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPostingItem } from 'app/shared/model/posting-item.model';
import { getEntities as getPostingItems } from 'app/entities/posting-item/posting-item.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { getEntity, updateEntity, createEntity, reset } from './post-favorite.reducer';
import { IPostFavorite } from 'app/shared/model/post-favorite.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPostFavoriteUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPostFavoriteUpdateState {
  isNew: boolean;
  postingItemId: string;
  userProfileId: string;
}

export class PostFavoriteUpdate extends React.Component<IPostFavoriteUpdateProps, IPostFavoriteUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      postingItemId: '0',
      userProfileId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getPostingItems();
    this.props.getUserProfiles();
  }

  saveEntity = (event, errors, values) => {
    values.dateFavorited = convertDateTimeToServer(values.dateFavorited);

    if (errors.length === 0) {
      const { postFavoriteEntity } = this.props;
      const entity = {
        ...postFavoriteEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/post-favorite');
  };

  render() {
    const { postFavoriteEntity, postingItems, userProfiles, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="haliApp.postFavorite.home.createOrEditLabel">Create or edit a PostFavorite</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : postFavoriteEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="post-favorite-id">ID</Label>
                    <AvInput id="post-favorite-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="dateFavoritedLabel" for="post-favorite-dateFavorited">
                    Date Favorited
                  </Label>
                  <AvInput
                    id="post-favorite-dateFavorited"
                    type="datetime-local"
                    className="form-control"
                    name="dateFavorited"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.postFavoriteEntity.dateFavorited)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="post-favorite-postingItem">Posting Item</Label>
                  <AvInput id="post-favorite-postingItem" type="select" className="form-control" name="postingItemId">
                    <option value="" key="0" />
                    {postingItems
                      ? postingItems.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="post-favorite-userProfile">User Profile</Label>
                  <AvInput id="post-favorite-userProfile" type="select" className="form-control" name="userProfileId">
                    <option value="" key="0" />
                    {userProfiles
                      ? userProfiles.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/post-favorite" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  postingItems: storeState.postingItem.entities,
  userProfiles: storeState.userProfile.entities,
  postFavoriteEntity: storeState.postFavorite.entity,
  loading: storeState.postFavorite.loading,
  updating: storeState.postFavorite.updating,
  updateSuccess: storeState.postFavorite.updateSuccess
});

const mapDispatchToProps = {
  getPostingItems,
  getUserProfiles,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PostFavoriteUpdate);
