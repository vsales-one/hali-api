import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICategory } from 'app/shared/model/category.model';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { getEntity, updateEntity, createEntity, reset } from './posting-item.reducer';
import { IPostingItem } from 'app/shared/model/posting-item.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPostingItemUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPostingItemUpdateState {
  isNew: boolean;
  categoryId: string;
}

export class PostingItemUpdate extends React.Component<IPostingItemUpdateProps, IPostingItemUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      categoryId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getCategories();
  }

  saveEntity = (event, errors, values) => {
    values.last_modified_date = convertDateTimeToServer(values.last_modified_date);
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);

    if (errors.length === 0) {
      const { postingItemEntity } = this.props;
      const entity = {
        ...postingItemEntity,
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
    this.props.history.push('/entity/posting-item');
  };

  render() {
    const { postingItemEntity, categories, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="haliApp.postingItem.home.createOrEditLabel">Create or edit a PostingItem</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : postingItemEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="posting-item-id">ID</Label>
                    <AvInput id="posting-item-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="titleLabel" for="posting-item-title">
                    Title
                  </Label>
                  <AvField
                    id="posting-item-title"
                    type="text"
                    name="title"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="imageUrlLabel" for="posting-item-imageUrl">
                    Image Url
                  </Label>
                  <AvField id="posting-item-imageUrl" type="text" name="imageUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="last_modified_dateLabel" for="posting-item-last_modified_date">
                    Last Modified Date
                  </Label>
                  <AvInput
                    id="posting-item-last_modified_date"
                    type="datetime-local"
                    className="form-control"
                    name="last_modified_date"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.postingItemEntity.last_modified_date)}
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="last_modified_byLabel" for="posting-item-last_modified_by">
                    Last Modified By
                  </Label>
                  <AvField
                    id="posting-item-last_modified_by"
                    type="text"
                    name="last_modified_by"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="posting-item-description">
                    Description
                  </Label>
                  <AvField id="posting-item-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="pickUpTimeLabel" for="posting-item-pickUpTime">
                    Pick Up Time
                  </Label>
                  <AvField
                    id="posting-item-pickUpTime"
                    type="text"
                    name="pickUpTime"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="startDateLabel" for="posting-item-startDate">
                    Start Date
                  </Label>
                  <AvInput
                    id="posting-item-startDate"
                    type="datetime-local"
                    className="form-control"
                    name="startDate"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.postingItemEntity.startDate)}
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="endDateLabel" for="posting-item-endDate">
                    End Date
                  </Label>
                  <AvInput
                    id="posting-item-endDate"
                    type="datetime-local"
                    className="form-control"
                    name="endDate"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.postingItemEntity.endDate)}
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="pickupAddressLabel" for="posting-item-pickupAddress">
                    Pickup Address
                  </Label>
                  <AvField
                    id="posting-item-pickupAddress"
                    type="text"
                    name="pickupAddress"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="latitudeLabel" for="posting-item-latitude">
                    Latitude
                  </Label>
                  <AvField id="posting-item-latitude" type="text" name="latitude" />
                </AvGroup>
                <AvGroup>
                  <Label id="longitudeLabel" for="posting-item-longitude">
                    Longitude
                  </Label>
                  <AvField id="posting-item-longitude" type="text" name="longitude" />
                </AvGroup>
                <AvGroup>
                  <Label for="posting-item-category">Category</Label>
                  <AvInput id="posting-item-category" type="select" className="form-control" name="categoryId" required>
                    {categories
                      ? categories.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.categoryName}
                          </option>
                        ))
                      : null}
                  </AvInput>
                  <AvFeedback>This field is required.</AvFeedback>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/posting-item" replace color="info">
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
  categories: storeState.category.entities,
  postingItemEntity: storeState.postingItem.entity,
  loading: storeState.postingItem.loading,
  updating: storeState.postingItem.updating,
  updateSuccess: storeState.postingItem.updateSuccess
});

const mapDispatchToProps = {
  getCategories,
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
)(PostingItemUpdate);
