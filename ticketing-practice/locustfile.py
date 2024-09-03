from locust import HttpUser, TaskSet, task, between

class DistrictBehavior(TaskSet):
    @task
    def get_districts_by_concert_id(self):
        response = self.client.get("/concerts/1/districts")

        # 상태 코드가 200이 아닌 경우
        if response.status_code != 200:
            print(f"Getting Districts By ConcertId failed with status code {response.status_code}. Response: {response.text}")
        assert response.status_code == 200

    @task
    def get_district(self):
        response = self.client.get("/districts/1")

        # 상태 코드가 200이 아닌 경우
        if response.status_code != 200:
            print(f"Getting Districts By ConcertId failed with status code {response.status_code}. Response: {response.text}")
        assert response.status_code == 200

class SeatBehavior(TaskSet):
    @task
    def get_seats_by_district_id(self):
        response = self.client.get("/districts/1/seats")

        # 상태 코드가 200이 아닌 경우
        if response.status_code != 200:
            print(f"Getting Seats By DistrictId By ConcertId failed with status code {response.status_code}. Response: {response.text}")
        assert response.status_code == 200

    # @task
    # def get_seat(self):
    #     response = self.client.get("/seats/1")
        
    #     # 상태 코드가 200이 아닌 경우
    #     if response.status_code != 200:
    #         print(f"Getting Seat failed with status code {response.status_code}. Response: {response.text}")
    #     assert response.status_code == 200

class WebsiteUser(HttpUser):
    tasks = [DistrictBehavior]
    tasks = [SeatBehavior]
    # 대기 시간을 0으로 설정하여 최대 동시성 테스트
    wait_time = between(0, 0)