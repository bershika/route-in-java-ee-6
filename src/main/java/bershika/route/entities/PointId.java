package bershika.route.entities;

import java.io.Serializable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PointId implements Serializable{
		@Size(min = 1, max = 50)
		@Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
		private String hubName;
		@Size(min = 2, max = 2)
		@Pattern(regexp = "[A-Z]{2}", message = "state must contain two upper letters")
		private String hubState;
		@Size(min = 1, max = 50)
		@Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
		private String destName;
		@Size(min = 2, max = 2)
		@Pattern(regexp = "[A-Z]{2}", message = "state must contain two upper letters")
		private String destState;
		private float rate;
		
		
		
		public String getHubName() {
			return hubName;
		}

		public void setHubName(String hubName) {
			this.hubName = hubName;
		}

		public String getHubState() {
			return hubState;
		}

		public void setHubState(String hubState) {
			this.hubState = hubState;
		}

		public String getDestName() {
			return destName;
		}

		public void setDestName(String destName) {
			this.destName = destName;
		}

		public String getDestState() {
			return destState;
		}

		public void setDestState(String destState) {
			this.destState = destState;
		}

		public float getRate() {
			return rate;
		}

		public void setRate(float rate) {
			this.rate = rate;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((destName == null) ? 0 : destName.hashCode());
			result = prime * result
					+ ((destState == null) ? 0 : destState.hashCode());
			result = prime * result
					+ ((hubName == null) ? 0 : hubName.hashCode());
			result = prime * result
					+ ((hubState == null) ? 0 : hubState.hashCode());
			result = prime * result + Float.floatToIntBits(rate);
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PointId other = (PointId) obj;
			if (destName == null) {
				if (other.destName != null)
					return false;
			} else if (!destName.equals(other.destName))
				return false;
			if (destState == null) {
				if (other.destState != null)
					return false;
			} else if (!destState.equals(other.destState))
				return false;
			if (hubName == null) {
				if (other.hubName != null)
					return false;
			} else if (!hubName.equals(other.hubName))
				return false;
			if (hubState == null) {
				if (other.hubState != null)
					return false;
			} else if (!hubState.equals(other.hubState))
				return false;
			if (Float.floatToIntBits(rate) != Float.floatToIntBits(other.rate))
				return false;
			return true;
		}
		
}
