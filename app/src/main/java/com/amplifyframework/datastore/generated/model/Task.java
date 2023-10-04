package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Task type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tasks", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byTeam", fields = {"teamId","title"})
public final class Task implements Model {
  public static final QueryField ID = field("Task", "id");
  public static final QueryField TITLE = field("Task", "title");
  public static final QueryField BODY = field("Task", "body");
  public static final QueryField DATE_CREATED = field("Task", "dateCreated");
  public static final QueryField STATUS = field("Task", "status");
  public static final QueryField TEAM = field("Task", "teamId");
  public static final QueryField TASK_IMAGE_S3_KEY = field("Task", "taskImageS3Key");
  public static final QueryField LATITUDE = field("Task", "latitude");
  public static final QueryField LONGITUDE = field("Task", "longitude");
  public static final QueryField ADDRESS = field("Task", "address");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String") String body;
  private final @ModelField(targetType="AWSDateTime") Temporal.DateTime dateCreated;
  private final @ModelField(targetType="TaskStatusEnum") TaskStatusEnum status;
  private final @ModelField(targetType="Team") @BelongsTo(targetName = "teamId", targetNames = {"teamId"}, type = Team.class) Team team;
  private final @ModelField(targetType="String") String taskImageS3Key;
  private final @ModelField(targetType="String") String latitude;
  private final @ModelField(targetType="String") String longitude;
  private final @ModelField(targetType="String") String address;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getBody() {
      return body;
  }
  
  public Temporal.DateTime getDateCreated() {
      return dateCreated;
  }
  
  public TaskStatusEnum getStatus() {
      return status;
  }
  
  public Team getTeam() {
      return team;
  }
  
  public String getTaskImageS3Key() {
      return taskImageS3Key;
  }
  
  public String getLatitude() {
      return latitude;
  }
  
  public String getLongitude() {
      return longitude;
  }
  
  public String getAddress() {
      return address;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Task(String id, String title, String body, Temporal.DateTime dateCreated, TaskStatusEnum status, Team team, String taskImageS3Key, String latitude, String longitude, String address) {
    this.id = id;
    this.title = title;
    this.body = body;
    this.dateCreated = dateCreated;
    this.status = status;
    this.team = team;
    this.taskImageS3Key = taskImageS3Key;
    this.latitude = latitude;
    this.longitude = longitude;
    this.address = address;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Task task = (Task) obj;
      return ObjectsCompat.equals(getId(), task.getId()) &&
              ObjectsCompat.equals(getTitle(), task.getTitle()) &&
              ObjectsCompat.equals(getBody(), task.getBody()) &&
              ObjectsCompat.equals(getDateCreated(), task.getDateCreated()) &&
              ObjectsCompat.equals(getStatus(), task.getStatus()) &&
              ObjectsCompat.equals(getTeam(), task.getTeam()) &&
              ObjectsCompat.equals(getTaskImageS3Key(), task.getTaskImageS3Key()) &&
              ObjectsCompat.equals(getLatitude(), task.getLatitude()) &&
              ObjectsCompat.equals(getLongitude(), task.getLongitude()) &&
              ObjectsCompat.equals(getAddress(), task.getAddress()) &&
              ObjectsCompat.equals(getCreatedAt(), task.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), task.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getBody())
      .append(getDateCreated())
      .append(getStatus())
      .append(getTeam())
      .append(getTaskImageS3Key())
      .append(getLatitude())
      .append(getLongitude())
      .append(getAddress())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Task {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("body=" + String.valueOf(getBody()) + ", ")
      .append("dateCreated=" + String.valueOf(getDateCreated()) + ", ")
      .append("status=" + String.valueOf(getStatus()) + ", ")
      .append("team=" + String.valueOf(getTeam()) + ", ")
      .append("taskImageS3Key=" + String.valueOf(getTaskImageS3Key()) + ", ")
      .append("latitude=" + String.valueOf(getLatitude()) + ", ")
      .append("longitude=" + String.valueOf(getLongitude()) + ", ")
      .append("address=" + String.valueOf(getAddress()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Task justId(String id) {
    return new Task(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      body,
      dateCreated,
      status,
      team,
      taskImageS3Key,
      latitude,
      longitude,
      address);
  }
  public interface TitleStep {
    BuildStep title(String title);
  }
  

  public interface BuildStep {
    Task build();
    BuildStep id(String id);
    BuildStep body(String body);
    BuildStep dateCreated(Temporal.DateTime dateCreated);
    BuildStep status(TaskStatusEnum status);
    BuildStep team(Team team);
    BuildStep taskImageS3Key(String taskImageS3Key);
    BuildStep latitude(String latitude);
    BuildStep longitude(String longitude);
    BuildStep address(String address);
  }
  

  public static class Builder implements TitleStep, BuildStep {
    private String id;
    private String title;
    private String body;
    private Temporal.DateTime dateCreated;
    private TaskStatusEnum status;
    private Team team;
    private String taskImageS3Key;
    private String latitude;
    private String longitude;
    private String address;
    @Override
     public Task build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Task(
          id,
          title,
          body,
          dateCreated,
          status,
          team,
          taskImageS3Key,
          latitude,
          longitude,
          address);
    }
    
    @Override
     public BuildStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep body(String body) {
        this.body = body;
        return this;
    }
    
    @Override
     public BuildStep dateCreated(Temporal.DateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }
    
    @Override
     public BuildStep status(TaskStatusEnum status) {
        this.status = status;
        return this;
    }
    
    @Override
     public BuildStep team(Team team) {
        this.team = team;
        return this;
    }
    
    @Override
     public BuildStep taskImageS3Key(String taskImageS3Key) {
        this.taskImageS3Key = taskImageS3Key;
        return this;
    }
    
    @Override
     public BuildStep latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }
    
    @Override
     public BuildStep longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }
    
    @Override
     public BuildStep address(String address) {
        this.address = address;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String body, Temporal.DateTime dateCreated, TaskStatusEnum status, Team team, String taskImageS3Key, String latitude, String longitude, String address) {
      super.id(id);
      super.title(title)
        .body(body)
        .dateCreated(dateCreated)
        .status(status)
        .team(team)
        .taskImageS3Key(taskImageS3Key)
        .latitude(latitude)
        .longitude(longitude)
        .address(address);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder body(String body) {
      return (CopyOfBuilder) super.body(body);
    }
    
    @Override
     public CopyOfBuilder dateCreated(Temporal.DateTime dateCreated) {
      return (CopyOfBuilder) super.dateCreated(dateCreated);
    }
    
    @Override
     public CopyOfBuilder status(TaskStatusEnum status) {
      return (CopyOfBuilder) super.status(status);
    }
    
    @Override
     public CopyOfBuilder team(Team team) {
      return (CopyOfBuilder) super.team(team);
    }
    
    @Override
     public CopyOfBuilder taskImageS3Key(String taskImageS3Key) {
      return (CopyOfBuilder) super.taskImageS3Key(taskImageS3Key);
    }
    
    @Override
     public CopyOfBuilder latitude(String latitude) {
      return (CopyOfBuilder) super.latitude(latitude);
    }
    
    @Override
     public CopyOfBuilder longitude(String longitude) {
      return (CopyOfBuilder) super.longitude(longitude);
    }
    
    @Override
     public CopyOfBuilder address(String address) {
      return (CopyOfBuilder) super.address(address);
    }
  }
  

  public static class TaskIdentifier extends ModelIdentifier<Task> {
    private static final long serialVersionUID = 1L;
    public TaskIdentifier(String id) {
      super(id);
    }
  }
  
}
